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
    private static final int totalMachines = 3;
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
    private Shell shell;
    /*
     * Token thats used to set whose machine turn is it
     */
    Token token;

    /*
     * Constructor is reponsible for initializing the most basic stuff a machine
     * needs
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
	    token = new Token(true, true, 0);
	} else {
	    token = new Token(false, true, null);
	}

	shell = new Shell();
	shell.start();

	listen = new ListenToConnection(this, false);
	listen.start();
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
		if (connection.getMachineId() == id) {
		    return connection;
		}
	    }
	} finally {
	    lock.readLock().unlock();
	}

	return null;
    }

    /**
     * Function will pass the token, if in its possession, to the next machine.
     */
    public void passToken() {
	Protocol.send(findNextMachine(), Protocol.TOKEN);
    }

    /**
     * Class is responsible for reading commands from STDIN
     * 
     * @author enio95
     *
     */
    private class Shell extends Thread {
	Scanner in;

	Shell() {
	    System.out.println("Passou");
	    in = new Scanner(System.in);
	}

	public void run() {
	    while (in.hasNext()) {
		Protocol.doAction(in.nextLine());
	    }

	    in.close();
	}
    }
}
