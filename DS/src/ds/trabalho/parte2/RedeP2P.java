package ds.trabalho.parte2;

public class RedeP2P {
    public static void main(String[] argv) throws Exception {
	int id = Integer.parseInt(argv[0]);
	int listenPort = Integer.parseInt(argv[1]);

	if (argv.length < 2) {
	    System.out.println(
		    "Usage: RedeP2P <machine number> <listening port>");
	    return;
	}

	Machine machine = new Machine(id, listenPort);
    }
}
