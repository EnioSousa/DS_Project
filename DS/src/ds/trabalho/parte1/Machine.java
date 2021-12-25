package ds.trabalho.parte1;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
     * Thread is responsible for starting the process of sending the token to
     * another machine
     */
    private Thread scheduleTokenPass;
    /**
     * The list of connection this machine has
     */
    private List<Connection> connections;
    /**
     * The ip table of other machines that we are connected
     */
    private List<InetAddress> ipTable;

    private Machine machine;

    private final int totalMachines = 5;

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
	this.machine = this;

	new Token(id == 1 ? true : false, false, 0);

	// Create a new shell thread
	shell = new Thread(new Runnable() {
	    @Override
	    public void run() {
		Scanner in = new Scanner(System.in);

		System.out.println("[INFO] Shell: Started:");

		while (in.hasNext()) {
		    Protocol.processCommand(machine, in.nextLine());
		}

		in.close();
		System.out.println("[INFO] Shell: Closed:");

		stopMachine();
	    }
	});
	shell.start();

	/**
	 * Responsible for trying to pass the token every second. Without this
	 * the network use is to high, if every machine doesn't have a lock
	 */
	scheduleTokenPass = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    passToken();
		    try {
			Thread.sleep(5000);
		    } catch (InterruptedException e) {
			System.err.print("[ERROR] Schedule: Sleepfailed:");
		    }
		}
	    }
	});
	scheduleTokenPass.start();

	// Create a new thread that will listen to connection requests
	listen = new Thread(new Runnable() {
	    @Override
	    public void run() {
		ServerSocket serverSocket = null;

		try {
		    serverSocket = new ServerSocket(listenPort);
		    System.out
			    .println("[INFO] Server: Start listening on port: "
				    + listenPort);

		    Socket socket = serverSocket.accept();

		    if (!haveConnection(socket.getInetAddress())) {

			Connection connection = new Connection(machine, socket,
				id);

			if (connection.isOpen()) {
			    System.out.println(
				    "[INFO] Server: Connection success: "
					    + socket.getInetAddress() + ": "
					    + socket.getPort());
			    addConnection(connection);
			} else {
			    System.out.println(
				    "[ERROR] Server: Conenction failed: "
					    + socket.getInetAddress() + ": "
					    + socket.getPort());
			}
		    } else {
			System.out.println("[ERROR] Server: Connection failed: "
				+ socket.getInetAddress() + ": "
				+ socket.getPort());
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    System.out.println("[INFO] Server: Stop listening on port: "
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
		if (haveConnection(ip)) {
		    System.out.println("[INFO] Connection: Already connected: "
			    + ip + ": " + port);
		    return;
		}

		boolean tryAgain = true;

		while (tryAgain) {
		    try {
			Socket socket = new Socket(ip, port);

			Connection connection = new Connection(machine, socket,
				id);

			if (connection.isOpen()) {
			    System.out.println(
				    "[INFO] Connection: Connection success: "
					    + socket.getInetAddress() + ": "
					    + socket.getPort());
			    addConnection(connection);
			    tryAgain = false;
			} else {
			    System.out.println(
				    "[ERROR] Connection: Connection failed: "
					    + socket.getInetAddress() + ": "
					    + socket.getPort());
			}
		    } catch (ConnectException e) {
			try {
			    Thread.sleep(3000);
			    System.out.println("[INFO] Connectionn: Retry: "
				    + ip + ": " + port);
			} catch (InterruptedException e1) {
			    System.err.println(
				    "[ERROR] Connection: Sleep interrupted:"
					    + e1);
			}
		    } catch (Exception e) {
			e.printStackTrace();
			tryAgain = false;
			System.out.println("[ERROR] Connection: FAIL: " + ip
				+ ": " + port);
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
	try {
	    if (ipTable.indexOf(ip) == -1
		    || ip.equals(InetAddress.getByName("localhost"))) {
		return false;
	    }
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}

	return true;
    }

    /**
     * Print our current ip table
     */
    public void showCurrentIpTable() {
	System.out.println("[INFO] Machine: Ip Table start:");
	int i = 0;
	for (InetAddress ip : ipTable) {
	    System.out.println("[INFO] Machine: Table Entry: " + i
		    + ": Table Value: " + ip);
	    i++;
	}
	System.out.println("[INFO] Machine: Ip table end:");
    }

    /**
     * Add a connection to ou list of connection
     * 
     * @param connection The connection to add
     */
    public void addConnection(Connection connection) {
	if (connections.add(connection)) {
	    ipTable.add(connection.getAddress());
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

    public void getMachineState() {
	showCurrentIpTable();
	Token.showCurrentState();
    }

    public Connection findNextMachine() {
	int nextMachine = id + 1 > totalMachines ? 1 : id + 1;
	return findMachine(nextMachine);
    }

    public Connection findMachine(int id) {
	for (Connection connection : connections) {
	    Integer num = connection.getOtherMachineId();

	    if (num != null && num == id) {
		return connection;
	    }
	}

	return null;
    }

    public void passToken() {
	Token.passToken(findNextMachine());
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
