package ds.trabalho.parte2;

public class RedeP2P {
    public static void main(String[] argv) throws Exception {
	int id = Integer.parseInt(argv[0]);
	int listenPort = Integer.parseInt(argv[1]);
	int port = Integer.parseInt(argv[3]);

	Machine machine = new Machine(id, listenPort);
    }
}
