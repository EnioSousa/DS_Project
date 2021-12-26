package ds.trabalho.parte2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReadChannel {
    /**
     * The buffer where we will read the messages
     */
    BufferedReader in;
    /**
     * Connection to the other machine
     */
    Connection connection;

    private Thread thread;

    private boolean stop = false;

    public ReadChannel(Connection connection) throws Exception {
	super();
	this.connection = connection;

	in = new BufferedReader(
		new InputStreamReader(connection.getSocket().getInputStream()));

	thread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (canContinue()) {
		    try {
			String str = in.readLine();

			if (str == null) {
			    connection.close();
			} else {
			    Protocol.proccessMessage(connection, str);
			}
		    } catch (Exception e) {
			e.printStackTrace();
			connection.close();
		    }
		}
	    }
	});

	thread.start();
    }

    void close() throws Exception {
	setStop(true);
	in.close();
	thread.interrupt();
    }

    private boolean canContinue() {
	return !isStop() && connection.getSocket() != null
		&& !connection.getSocket().isInputShutdown();
    }

    public boolean isStop() {
	return stop;
    }

    public void setStop(boolean stop) {
	this.stop = stop;
    }
}
