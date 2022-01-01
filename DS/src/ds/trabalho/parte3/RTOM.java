package ds.trabalho.parte3;

public class RTOM {
    public static void main(String[] argv) {
	ArgParse argParse = new ArgParse(argv);

	Machine machine = new Machine(argParse.getId(), argParse.getPort());

	// Construct network if ip set was given.
	if (argParse.getIpSet() != null) {
	    System.out.println("[STDOUT] Auto: Build network:");

	    for (int i = 1; i <= 4; i++) {
		if (i != argParse.getId()) {
		    machine.register(argParse.getIp(i));
		}
	    }
	}

    }
}
