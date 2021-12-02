package ds.trabalho.parte1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class is responsible for creating threads to read and write to sockets, since
 * those operations are blocking, and also has the possibility to initiate a
 * connection if an IP and Port is given
 * 
 * @author enio95
 *
 */
public class Connection {
    Machine machine;
    Socket socket;

    private Integer machineID;

    private Write write;
    private Read read;

    /**
     * Constructor receives a connection and tries to create the read and write
     * threads
     * 
     * @param machine Machine that created this object
     * @param socket  The socket connection with the other machine
     */
    public Connection(Machine machine, Socket socket) {
	this.machine = machine;
	this.socket = socket;

	startThreads();
    }

    /**
     * Constructor will try to initiate a new connection with another machine,
     * it also creates the read and write threads
     * 
     * @param machine The machine that created this object
     * @param ip      The ip of the other machine
     * @param port    the port to connect with the other machine
     */
    public Connection(Machine machine, InetAddress ip, int port) {
	this.machine = machine;

	new Thread(new Runnable() {
	    @Override
	    public void run() {
		System.out
			.println("Attempt connection with: " + ip + " " + port);

		boolean tryAgain = true;

		while (tryAgain) {
		    try {
			while (!ip.isReachable(3000))
			    ;

			socket = new Socket(ip, port);
			System.out.println("Connection attempt Success: " + ip
				+ " " + port);

			startThreads();
			tryAgain = false;

		    } catch (Exception e) {
			tryAgain = true;
		    }
		}

	    }
	}).start();
    }

    /*
     * Starts the read and write threads
     */
    private void startThreads() {
	read = new Read(this);
	read.start();

	write = new Write(this);
	write.start();
    }

    /**
     * Register the machine ID that we are CONNECTED
     * 
     * @param n
     */
    public void setMachineId(int n) {
	machineID = n;
    }

    /**
     * Get the machine id that we are connected
     * 
     * @return
     */
    public Integer getMachineId() {
	return machineID;
    }

    /**
     * Get socket
     * 
     * @return socket
     */
    public Socket getSocket() {
	return socket;
    }

    /**
     * Send a string through the connection
     * 
     * @param str String to send
     */
    public void send(String str) {
	write.write(str);
    }

    /**
     * Close the read and write threads
     */
    public void closeConnection() {
	if (write != null && !write.isInterrupted()) {
	    write.closeThread();
	    write = null;
	}

	if (read != null && !read.isInterrupted()) {
	    read.closeThread();
	    read = null;
	}

	try {
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    machine.delConnection(this);
	}
    }

    /**
     * Class is responsible for reading for a socket
     * 
     * @author enio95
     *
     */
    private class Read extends Thread {
	Connection connection;
	BufferedReader in;

	Read(Connection connection) {
	    this.connection = connection;
	}

	public void run() {
	    // Create read channel
	    try {
		in = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
	    } catch (IOException e) {
		e.printStackTrace();
		closeConnection();
	    }

	    // Read from channel
	    while (socket != null && !socket.isClosed()
		    && !socket.isInputShutdown() && in != null) {
		try {
		    Protocol.receive(connection, in.readLine());
		} catch (IOException e) {
		    if (read != null)
			closeConnection();
		}
	    }

	    closeConnection();
	}

	/**
	 * Close current thread
	 */
	private void closeThread() {
	    try {
		in.close();
		in = null;
	    } catch (IOException e) {
		e.printStackTrace();
	    } finally {
		System.out.println("Closed read thread");
		Thread.currentThread().interrupt();
	    }

	}
    }

    /**
     * Class is responsible for writing to a socket
     * 
     * @author enio95
     *
     */
    private class Write extends Thread {
	private Connection connection;
	private PrintWriter out;

	Write(Connection connection) {
	    this.connection = connection;
	}

	public void run() {
	    try {
		out = new PrintWriter(socket.getOutputStream(), true);
		Protocol.send(connection, Protocol.HELLO,
			String.valueOf(machine.getId()));
	    } catch (IOException e) {
		e.printStackTrace();
		closeConnection();
	    }
	}

	/**
	 * Write a string to a socket
	 * 
	 * @param str String to send
	 */
	public void write(String str) {
	    out.println(str);
	}

	private void closeThread() {
	    out.close();
	    out = null;
	    System.out.println("Closed write thread");
	    Thread.currentThread().interrupt();
	}
    }
}
