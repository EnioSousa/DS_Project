package ds.trabalho.parte3;

public class RedeP2P {
    public static void main(String[] argv) {
	Integer id = Integer.parseInt(argv[findInArray(argv, "--id") + 1]);
	Integer port = Integer
		.parseInt(argv[findInArray(argv, "--portListen") + 1]);

	Machine machine = new Machine(id, port);
    }

    public static Integer findInArray(String[] argv, String str) {
	for (int i = 0; i < argv.length; i++) {
	    if (argv[i].equals(str))
		return i;
	}

	return -1;
    }

}
