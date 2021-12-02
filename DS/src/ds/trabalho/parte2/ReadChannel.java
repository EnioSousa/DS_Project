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

    /**
     * Constructor will create the necessary buffers and start a new thread that
     * will listen for incoming messages
     * 
     * @param connection the connection to the other machine
     * @throws Exception
     */
    public ReadChannel(Connection connection) throws Exception {
	super();
	this.connection = connection;

	in = new BufferedReader(
		new InputStreamReader(connection.getSocket().getInputStream()));

	read();
    }

    /**
     * Method will create a new thread that will listen to messages and pass
     * them through a protocol
     */
    private void read() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		String str = "Something";
		while (!connection.getSocket().isInputShutdown()
			&& str != null) {
		    try {
			str = in.readLine();
			Protocol.receive(connection, str);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    }
	}).start();
    }
}
