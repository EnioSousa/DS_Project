package ds.trabalho.parte2;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class handles the machine. It creates a shell, to read commands from the
 * user, a listening thread, to listen to incoming connection request and is
 * able to connect to other machines. Its saves the connection information and
 * the dictionary info that will be shared between our network
 * 
 * @author enio95
 *
 */
public class Machine {
    /**
     * Id of our machine
     */
    private int id;
    /**
     * The listening port for incoming connection request
     */
    private int listenPort;
    /**
     * Thread that is responsible for reading commands from the user
     */
    private Thread shell;
    /**
     * Thread responsible for accepting connections
     */
    private Thread listen;
    /**
     * The list of connection this machine has
     */
    private List<Connection> connections;
    /**
     * The ip table of other machines that we are connected
     */
    private List<InetAddress> ipTable;
    /**
     * Lock to access the ipTable and connections list. There is concurrency
     */
    private ReentrantReadWriteLock listAccessLock = new ReentrantReadWriteLock();
    /**
     * The dictionary
     */
    Dictionary dic;
    private Machine machine;

    /**
     * Constructor will create a new listing thread, a shell thread and initiate
     * the dictionary
     * 
     * @param id         Id of our machine
     * @param listenPort The listing port for new connection request
     */
    public Machine(int id, int listenPort) {
	super();
	this.id = id;
	this.listenPort = listenPort;
	this.connections = new ArrayList<>();
	this.ipTable = new ArrayList<>();
	this.dic = new Dictionary();
	this.machine = this;

	Protocol.setCurMachine(this);

	// Create a new shell thread
	shell = new Thread(new Runnable() {
	    @Override
	    public void run() {
		Scanner in = new Scanner(System.in);

		System.out.println("[STDOUT: Shell: Started: ");

		while (in.hasNext()) {
		    Protocol.proccessCommand(in.nextLine());
		}

		in.close();
		System.out.println("[STDOUT]: Shell: Closed: ");

		stopMachine();
	    }
	});
	shell.start();

	// Create a new thread that will listen to connection requests
	listen = new Thread(new Runnable() {
	    @Override
	    public void run() {
		System.out.println("[INFO] Server: Start listening on port: "
			+ listenPort);
		ServerSocket serverSocket = null;

		try {
		    serverSocket = new ServerSocket(listenPort);

		    while (true) {
			Socket socket = serverSocket.accept();

			listAccessLock.writeLock().lock();

			try {
			    if (!haveConnection(socket.getInetAddress())) {

				Connection connection = new Connection(machine,
					socket, id);

				if (connection.isOpen()) {
				    System.out.println(
					    "[INFO] Server: Connection success: "
						    + socket.getInetAddress()
						    + ": " + socket.getPort());
				    addConnection(connection);
				} else {
				    System.out.println(
					    "[ERROR] Server: Conenction failed: "
						    + socket.getInetAddress()
						    + ": " + socket.getPort());
				}
			    } else {
				System.out.println(
					"[ERROR] Server: Already connected: "
						+ socket.getInetAddress());
				socket.close();
			    }
			} finally {
			    listAccessLock.writeLock().unlock();
			}

		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    System.out
			    .println("[INFO]: Server: Stop listening on port: "
				    + listenPort);
		    try {
			serverSocket.close();
		    } catch (IOException e) {
		    }
		}
	    }
	});
	listen.start();

    }

    /**
     * Method will attempt to connect with another machine. The connection is
     * only performed if the ip is not on the current ip table and if the other
     * machine is accepting a connection. In case the other machine does not
     * have a server waitng on that port we will retry after a given time
     * 
     * @param ip   ip to connect
     * @param port port to conenct
     */
    public void attemptConnection(InetAddress ip, int port) {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		boolean tryAgain = true;

		while (tryAgain) {
		    listAccessLock.writeLock().lock();

		    try {
			if (haveConnection(ip)) {
			    System.out.println(
				    "[INFO] Attempt: Already connected: " + ip
					    + ": " + port);
			    tryAgain = false;
			} else {
			    try {
				Socket socket = new Socket(ip, port);

				Connection connection = new Connection(machine,
					socket, id);

				if (connection.isOpen()) {
				    System.out.println(
					    "[INFO] Attempt: Connection success: "
						    + socket.getInetAddress()
						    + ": " + socket.getPort());
				    addConnection(connection);
				    tryAgain = false;
				} else {
				    System.out.println(
					    "[ERROR] Attempt: Connection failed: "
						    + socket.getInetAddress()
						    + ": " + socket.getPort());
				}

			    } catch (ConnectException e) {
				tryAgain = true;
			    } catch (Exception e) {
				e.printStackTrace();
				tryAgain = false;
				System.out.println("[ERROR] Attempt: FAIL: "
					+ ip + ": " + port);
			    }
			}

		    } finally {
			listAccessLock.writeLock().unlock();
		    }

		    try {
			Thread.sleep(3000);
			if (tryAgain)
			    System.out.println("[INFO] Attempt: Retry: " + ip
				    + ": " + port);
		    } catch (InterruptedException e1) {
			System.err.println(
				"[ERROR] Attempt: Sleep interrupted:" + e1);
		    }
		}
	    }
	}).start();
    }

    /**
     * Checks if we already have a connection to another machine that has the
     * given ip
     * 
     * @param ip ip to check
     * @return true if ip is on ip table, otherwise false
     */
    public boolean haveConnection(InetAddress ip) {
	for (InetAddress iteIp : getIpTable()) {
	    System.out.println(iteIp.getHostAddress());
	    System.out.println(ip.getHostAddress());
	    if (iteIp.getHostAddress().equals(ip.getHostAddress()))
		return true;
	}

	return false;
    }

    /**
     * Print our current ip table
     */
    public void showCurrentIpTable() {
	System.out.println("[STDOUT] Machine: Ip Table start:");
	int i = 0;
	for (InetAddress ip : ipTable) {
	    System.out.println("[STDOUT] Machine: Table Entry: " + i
		    + ": Table Value: " + ip);
	    i++;
	}
	System.out.println("[STDOUT] Machine: Ip table end:");
    }

    /**
     * Add a connection to ou list of connection
     * 
     * @param connection The connection to add
     */
    public void addConnection(Connection connection) {
	if (connections.add(connection)) {
	    ipTable.add(connection.getAddress());
	    Protocol.sendMessage(connection, Protocol.MSG_HELLO,
		    String.valueOf(id));
	}
    }

    /**
     * Remove a connection from our list of connections
     * 
     * @param connection connection to remove
     */
    public void remConnection(Connection connection) {
	if (connections.remove(connection)) {
	    ipTable.remove(connection.getAddress());
	    System.out.println("[INFO] Machine: Lost connection: "
		    + connection.getAddress());
	}

	if (connections.size() == 0)
	    stopMachine();
    }

    public void stopMachine() {
	for (Connection connection : connections) {
	    connection.close();
	}

	System.out.println("[INFO] Machine: Stop:");
	System.exit(0);
    }

    /**
     * Attempt connection with another machine. Uses default port(listenPort)
     * 
     * @param ip ip to connect
     */
    public void register(String ip) {
	register(ip, listenPort);
    }

    /**
     * Attempt connection with another machine
     * 
     * @param ip   ip to connect in string
     * @param port port to connect
     */
    public void register(String ip, int port) {
	try {
	    register(InetAddress.getByName(ip), port);
	} catch (UnknownHostException e) {
	    System.out.println("[ERRO] Machine: Could not find host address: "
		    + ip + ": " + port);
	}
    }

    /**
     * Attemp connection another another machine
     * 
     * @param ip inetaddress to connect
     */
    public void register(InetAddress ip, int port) {
	attemptConnection(ip, port);
    }

    /**
     * Get the connection to another machine
     * 
     * @param ip the ip to find
     * @return A connection with another machine that has the same ip
     */
    public Connection findConnection(String ip) {
	Connection connection = null;

	try {
	    connection = findConnection(InetAddress.getByName(ip));
	} catch (Exception e) {
	}

	return connection;
    }

    /**
     * Get the connection to another machine
     * 
     * @param ip InetAddress of the other machine we want to find
     * @return a connection with another machine
     */
    public Connection findConnection(InetAddress ip) {
	for (Connection connection : connections) {
	    if (connection.getSocket().getInetAddress().equals(ip))
		return connection;
	}

	return null;
    }

    /**
     * Get the connection to another machine
     * 
     * @param id the id to match
     * @return a connection with another machine
     */
    public Connection findConnection(Integer id) {
	for (Connection connection : connections) {
	    if (connection.getOtherMachineId().equals(id))
		return connection;
	}

	return null;
    }

    public void getMachineState() {
	Dictionary.showDic();
	showCurrentIpTable();
    }

    /**
     * Clones and return the connection list. There are some problems with
     * concurrency, so unfortunatly we need to clone
     * 
     * @return
     */
    public List<Connection> getConnections() {
	return new ArrayList<Connection>(connections);
    }

    /**
     * Clones and return the ipTable list. There are some problems with
     * concurrency, so unfortunatly we need to clone
     * 
     * @return
     */
    public List<InetAddress> getIpTable() {
	return new ArrayList<InetAddress>(ipTable);
    }

    /**
     * Get machine id
     * 
     * @return
     */
    public int getId() {
	return id;
    }

    /**
     * Set machine id
     * 
     * @param id
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * Get listing port
     * 
     * @return
     */
    public int getListenPort() {
	return listenPort;
    }

}
