package ds.trabalho.parte2;

import java.io.PrintWriter;

public class WriteChannel {
    /**
     * Connection to the other machine
     */
    private Connection connection;
    /**
     * Object used to write
     */
    private PrintWriter out;

    Thread writeThread;

    public WriteChannel(Connection connection) throws Exception {
	super();
	this.connection = connection;

	out = new PrintWriter(connection.getSocket().getOutputStream(), true);
    }

    /**
     * Method initiates a new thread that is responsible for writing in the
     * socket.
     * 
     * @param string
     */
    public void write(String string) {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		if (!connection.getSocket().isOutputShutdown()) {
		    out.println(string);
		}
	    }
	}).start();
    }

}
