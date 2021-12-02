package ds.trabalho.parte1;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is responsible for holding pertinent information about the current
 * machine. Its responsible for creating the appropriate thread for
 * inter-machine communications.
 * 
 * @author enio95
 *
 */
public class Machine {
    /**
     * Machine id
     */
    private static int id;
    /*
     * Number of total machines in the token ring
     */
    private static final int totalMachines = 6;
    /*
     * Listening port for new connections
     */
    private int listenPort;
    /*
     * Thread responsible for listening to new connections
     */
    private ListenToConnection listen;
    /*
     * A list of established connections with other machines
     */
    private static List<Connection> connections = new ArrayList<>();
    /*
     * A lock for the list of connections
     */
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    /*
     * Thread responsible for reading commands from the user
     */
    private Thread shell;
    /**
     * Thread is responsible for starting the process of sending the token to
     * another machine
     */
    private Thread scheduleTokenPass;
    /*
     * Time to wait before we trying to send the token to another machine
     */
    private long waitTime = 2000;
    /*
     * Token thats used to set whose machine turn is it
     */
    Token token;

    /*
     * Constructor is responsible for initialising the most basic stuff a
     * machine needs
     * 
     * @param id Machine ID
     * 
     * @param listenPort Which port the machine is going to listen
     */
    public Machine(int id, int listenPort) {
	super();
	Machine.id = id;
	this.listenPort = listenPort;

	if (id == 0) {
	    token = new Token(true, false, 0);
	} else {
	    token = new Token(false, false, null);
	}

	listen = new ListenToConnection(this, false);
	listen.start();

	/**
	 * Reads commands from stdin
	 */
	shell = new Thread(new Runnable() {
	    @Override
	    public void run() {
		Scanner in = new Scanner(System.in);

		System.out.println("Command shell started");

		while (in.hasNext()) {
		    Protocol.doAction(in.nextLine());
		}

		in.close();
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
		    if (Token.isTokenOwn() && !Token.isTokenLock()) {
			passToken();
		    } else {
			try {
			    Thread.sleep(waitTime);
			} catch (InterruptedException e) {
			    System.err.print("Sleepfailed");
			}
		    }
		}
	    }
	});
	scheduleTokenPass.start();
    }

    private void stopMachine() {
	System.out.println("Stoping machine");
	for (Connection connection : connections) {
	    connection.closeConnection();
	}

	scheduleTokenPass.interrupt();
	System.out.println("Connections closed");
    }

    /**
     * Get the machine ID
     * 
     * @return ID of the machine
     */
    public int getId() {
	return id;
    }

    /**
     * Get the port that the this machines is listening to
     * 
     * @return listening port
     */
    public int getListenPort() {
	return listenPort;
    }

    @Override
    public String toString() {
	return "Machine [id=" + id + ", " + ", listenPort=" + listenPort + "]";
    }

    /**
     * Connects the current machine to another machine
     * 
     * @param ip   IP of the other machine
     * @param port Port of the other machine
     */
    public void connectTo(InetAddress ip, int port) {
	addConnection(new Connection(this, ip, port));
    }

    /**
     * Add a connection to another machine to our list of connections
     * 
     * @param connection A connection to another machine
     */
    public void addConnection(Connection connection) {
	lock.writeLock().lock();

	try {
	    Machine.connections.add(connection);
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Remove a connection from our list
     * 
     * @param connection A connection to another machine
     */
    public void delConnection(Connection connection) {
	lock.writeLock().lock();

	try {
	    Machine.connections.remove(connection);
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Find the connection to the machine that is next to ours. Example: If our
     * machine is m2 then this function return m3 or m0
     * 
     * @return The connection the next machine if exists
     */
    public static Connection findNextMachine() {
	return findMachine((id + 1) % totalMachines);
    }

    /**
     * Given an ID, this function will find the connection that has a machine
     * that matches the given ID
     * 
     * @param id ID of the other machine
     * @return Connection to the other machine
     */
    private static Connection findMachine(int id) {
	lock.readLock().lock();

	try {
	    for (Connection connection : connections) {
		if (connection.getMachineId() != null
			&& connection.getMachineId() == id) {
		    return connection;
		}
	    }
	} finally {
	    lock.readLock().unlock();
	}

	return null;
    }

    public void closeProgram() {

    }

    /**
     * Function will pass the token, if in its possession, to the next machine.
     */
    public void passToken() {
	Protocol.send(findNextMachine(), Protocol.TOKEN);
    }
}
