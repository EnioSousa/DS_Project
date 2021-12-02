package ds.trabalho.parte2;

import java.io.PrintWriter;

public class WriteChannel {
    private Connection connection;
    private PrintWriter out;

    Thread writeThread;

    public WriteChannel(Connection connection) throws Exception {
	super();
	this.connection = connection;

	out = new PrintWriter(connection.getSocket().getOutputStream(), true);
    }

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
