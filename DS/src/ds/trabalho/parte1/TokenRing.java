package ds.trabalho.parte1;

import java.net.InetAddress;

public class TokenRing {
    public static void main(String[] argv) throws Exception {
	int id = Integer.parseInt(argv[0]);
	int listenPort = Integer.parseInt(argv[1]);
	int port = Integer.parseInt(argv[3]);

	Machine machine = new Machine(id, listenPort);
	machine.connectTo(InetAddress.getByName(argv[2]), port);
    }
}
