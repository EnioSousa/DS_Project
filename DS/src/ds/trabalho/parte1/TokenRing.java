package ds.trabalho.parte1;

import java.net.InetAddress;

public class TokenRing {
    public static void main(String[] argv) throws Exception {
	String ip = argv[findInArray(argv, "--ip") + 1];
	Integer port = Integer.parseInt(argv[findInArray(argv, "--port") + 1]);
	Integer listenPort = Integer
		.parseInt(argv[findInArray(argv, "--listenPort") + 1]);
	Integer id = Integer.parseInt(argv[findInArray(argv, "--id") + 1]);

	Machine machine = new Machine(id, listenPort);

	machine.attemptConnection(InetAddress.getByName(ip), port);
    }

    public static Integer findInArray(String[] argv, String str) {
	for (int i = 0; i < argv.length; i++) {
	    if (argv[i].equals(str))
		return i;
	}

	return -1;
    }
}
