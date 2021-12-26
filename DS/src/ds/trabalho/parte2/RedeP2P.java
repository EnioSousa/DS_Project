package ds.trabalho.parte2;

public class RedeP2P {
    public static void main(String[] argv) {
	ArgParse argParse = new ArgParse(argv);

	Machine machine = new Machine(argParse.getId(), argParse.getPort());

	if (argParse.getIpSet() != null) {
	    System.out.println("[STDOUT] Auto: Build network:");

	    switch (argParse.getId()) {
	    case 2:
		machine.register(argParse.getIp(1));
		machine.register(argParse.getIp(3));
		machine.register(argParse.getIp(4));
		break;

	    case 4:
		machine.register(argParse.getIp(5));
		machine.register(argParse.getIp(6));
		break;

	    default:
		break;
	    }

	}
    }

}
