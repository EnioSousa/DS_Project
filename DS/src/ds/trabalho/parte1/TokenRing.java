package ds.trabalho.parte1;

public class TokenRing {
    public static void main(String[] argv) throws Exception {
	ArgParse argParse = new ArgParse(argv);

	Machine machine = new Machine(argParse.getId(), argParse.getPort());

	if (argParse.getIpSet() != null) {
	    System.out.println("[STDOUT] Auto: Build network:");

	    if (argParse.getId() != argParse.getIpSet().length) {
		machine.register(argParse.getIp(argParse.getId() + 1));
	    } else {
		machine.register(argParse.getIp(1));
	    }
	}
    }
}
