package ds.trabalho.parte3;

public class RTOM {
    public static void main(String[] argv) {
	Integer id = Integer.parseInt(argv[findInArray(argv, "--id") + 1]);
	Integer port = Integer
		.parseInt(argv[findInArray(argv, "--listenPort") + 1]);
	String[] ip = argv[findInArray(argv, "--ip") + 1].split(",");

	Machine machine = new Machine(id, port);

	for (String str : ip) {
	    machine.register(str);
	}
    }

    public static Integer findInArray(String[] argv, String str) {
	for (int i = 0; i < argv.length; i++) {
	    if (argv[i].equals(str))
		return i;
	}

	return -1;
    }
}
