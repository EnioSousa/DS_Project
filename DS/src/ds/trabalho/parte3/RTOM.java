package ds.trabalho.parte3;

public class RTOM {
    public static void main(String[] argv) {
	ArgParse argParse = new ArgParse(argv);

	Machine machine = new Machine(argParse.getId(), argParse.getPort());

	for (int i = 1; i <= 4; i++) {
	    if (i != argParse.getId()) {
		machine.register(argParse.getIp(i));
	    }
	}

    }
}
