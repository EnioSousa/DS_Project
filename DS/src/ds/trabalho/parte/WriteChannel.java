package ds.trabalho.parte;

import java.io.PrintWriter;

public class WriteChannel {
    /**
     * Connection to the another machine
     */
    private Connection connection;
    /**
     * Print stream to the other machine
     */
    private PrintWriter out;

    /**
     * Constructor creates a printing stream
     * 
     * @param connection Connection to another machine
     * @throws Exception Failed to create the print stream
     */
    public WriteChannel(Connection connection) throws Exception {
	this.connection = connection;

	out = new PrintWriter(connection.getSocket().getOutputStream());
    }

    /**
     * Sends a string to another machine
     * 
     * @param string The string to send
     * @param block  If true the call will block, otherwise is non blocking
     */
    void write(String string, boolean block) {
	if (block) {
	    if (!connection.getSocket().isOutputShutdown()) {
		out.println(string);
	    }

	} else {
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    if (!connection.getSocket().isOutputShutdown()) {
			out.println(string);
			out.flush();
		    }
		}
	    }).start();
	}
    }

    /**
     * Close the writing channel
     */
    void close() {
	out.close();
    }

}
