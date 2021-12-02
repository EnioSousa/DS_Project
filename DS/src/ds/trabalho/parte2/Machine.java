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
     * The dictionary
     */
    Dictionary dic;

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

	Protocol.setCurMachine(this);

	// Create a new shell thread
	shell = new Thread(new Runnable() {
	    @Override
	    public void run() {
		Scanner in = new Scanner(System.in);

		System.out.println("SHELL STARTED");

		while (in.hasNext()) {
		    Protocol.doAction(in.nextLine());
		}

		in.close();
		System.out.println("SHELL CLOSED");
	    }
	});
	shell.start();

	// Create a new thread that will listen to connection requests
	listen = new Thread(new Runnable() {
	    @Override
	    public void run() {
		System.out.println("START LISTENING ON PORT: " + listenPort);
		ServerSocket serverSocket = null;

		try {
		    serverSocket = new ServerSocket(listenPort);

		    while (true) {
			Socket socket = serverSocket.accept();

			if (!haveConnection(socket.getInetAddress())) {

			    addConnection(new Connection(socket, id));

			    ipTable.add(socket.getInetAddress());

			    System.out.println(
				    "ACCEPTED: " + socket.getInetAddress() + " "
					    + socket.getPort());
			} else {
			    System.out.println(
				    "REJECTED:" + socket.getInetAddress() + " "
					    + socket.getPort());
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    System.out.println("STOP LISTENING ON PORT: " + listenPort);
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
		    System.out.println("ALREADY CONNECTED: " + ip + " " + port);
		    return;
		}

		System.out.println("ATTEMPT: " + ip + " " + port);

		boolean tryAgain = true;

		while (tryAgain) {
		    try {
			Socket socket = new Socket(ip, port);
			System.out.println("SUCCESS: " + ip + " " + port);

			addConnection(new Connection(socket, id));

			ipTable.add(ip);

			tryAgain = false;
		    } catch (ConnectException e) {
			try {
			    Thread.sleep(3000);
			    System.out.println("RETRY: " + ip + " " + port);
			} catch (InterruptedException e1) {
			    System.err.println("Sleep interrupted:" + e1);
			}
		    } catch (Exception e) {
			e.printStackTrace();
			tryAgain = false;
			System.out.println("FAIL: " + ip + " " + port);
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
	System.out.println("Ip Table start:");
	int i = 0;
	for (InetAddress ip : ipTable) {
	    System.out.println("Entry: " + i + " " + ip);
	    i++;
	}
	System.out.println("Ip table end:");
    }

    /**
     * Print our current disc. Be careful method is blocking, since several
     * thread have access to our dictionary
     */
    public void showCurrentDic() {
	dic.showDic();
    }

    /**
     * Add a connection to ou list of connection
     * 
     * @param connection The connection to add
     */
    public void addConnection(Connection connection) {
	connections.add(connection);
    }

    /**
     * Remove a connection from our list of connections
     * 
     * @param connection connection to remove
     */
    public void remConnection(Connection connection) {
	connections.remove(connection);
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

    /**
     * Add word to our dictionary. Method blocks thread
     * 
     * @param word word to add
     */
    public void addWord(String word) {
	dic.addWord(word);
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
	}
    }

    /**
     * Attemp connection another another machine
     * 
     * @param ip   inetaddress to connect
     * @param port port to connect
     */
    public void register(InetAddress ip, int port) {
	attemptConnection(ip, port);
    }

    /**
     * Send current dictionary to another machine
     * 
     * @param ip ip(string) of the other machine
     */
    public void push(String ip) {
	push(findConnection(ip));
    }

    /**
     * Send current dictionary to another machine
     * 
     * @param id the other machine id
     */
    public void push(int id) {
	push(findConnection(id));
    }

    public void push(Connection connection) {
	if (connection == null) {
	    System.out.println("Machine not found");
	    return;
	}

	new Thread(new Runnable() {
	    @Override
	    public void run() {
		dic.lock.readLock().lock();

		try {
		    for (String string : dic.getDic()) {
			Protocol.send(connection, Protocol.MSG_TABLE, string);
		    }
		} finally {
		    System.out.println("Pushed has finished");
		    dic.lock.readLock().unlock();
		}
	    }
	}).start();

    }

    /**
     * Request another machine dictionary
     * 
     * @param ip The ip of the other machine
     */
    public void pull(String ip) {
	pull(findConnection(ip));
    }

    /**
     * Request another machine dictionary
     * 
     * @param id the id of the other machine
     */
    public void pull(int id) {
	pull(findConnection(id));
    }

    /**
     * Request another machine dictionary
     * 
     * @param connection The connection with the other machine
     */
    public void pull(Connection connection) {
	if (connection == null) {
	    System.out.println("Machine not found");
	    return;
	}
	Protocol.send(connection, Protocol.MSG_SEND_TABLE, null);

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

	    else
		System.out.println(
			connection.getSocket().getInetAddress() + " " + ip);
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

}
