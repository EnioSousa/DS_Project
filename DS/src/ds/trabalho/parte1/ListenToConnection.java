package ds.trabalho.parte1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class is responsible for listening request for connection in a given port
 * 
 * @author enio95
 *
 */
public class ListenToConnection extends Thread {
    ServerSocket serverSocket;
    Socket socket;
    /*
     * The machine that created this thread
     */
    Machine machine;
    /*
     * If true then we will accept multiple request otherwise only one
     */
    boolean multipleAccept;

    /**
     * Constructor
     * 
     * @param machine        Machine object that created this thread
     * @param multipleAccept true if we want to accept multiple connections
     *                       otherwise false
     */
    public ListenToConnection(Machine machine, boolean multipleAccept) {
	super();
	this.machine = machine;
	this.multipleAccept = multipleAccept;
    }

    public void run() {
	try {
	    this.serverSocket = new ServerSocket(machine.getListenPort());
	} catch (Exception e) {
	    e.printStackTrace();
	    closeThread();
	    return;
	}

	do {
	    try {
		messageStartListening();
		socket = serverSocket.accept();
		messageAcceptConnection();

		machine.addConnection(new Connection(machine, socket));
	    } catch (IOException e) {
		e.printStackTrace();
		closeThread();
	    }

	} while (multipleAccept);

	messageStopListening();

    }

    /**
     * Method will close the current thread
     */
    private void closeThread() {
	try {
	    if (serverSocket != null && !serverSocket.isClosed())
		serverSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    messageStopListening();
	    Thread.currentThread().interrupt();
	}
    }

    /**
     * Message to indicate that we are listening on a port
     */
    private void messageStartListening() {
	System.out
		.println("Start listening on port: " + machine.getListenPort());
    }

    /**
     * Message to indicate that we stoped listening
     */
    private void messageStopListening() {
	System.out
		.println("Stop listening on port: " + machine.getListenPort());
    }

    /**
     * Message to indicate that we accepted a connection
     */
    private void messageAcceptConnection() {
	System.out.println("Accept: " + socket.getInetAddress());
    }

}
