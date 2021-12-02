package ds.trabalho.parte2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReadChannel {
    BufferedReader in;
    Connection connection;

    public ReadChannel(Connection connection) throws Exception {
	super();
	this.connection = connection;

	in = new BufferedReader(
		new InputStreamReader(connection.getSocket().getInputStream()));

	read();
    }

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
