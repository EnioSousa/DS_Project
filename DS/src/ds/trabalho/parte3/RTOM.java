package ds.trabalho.parte3;

import java.util.Random;
import java.util.concurrent.TimeUnit;

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

	if (findInArray(argv, "--test") > 0) {
	    Random random = new Random();

	    try {
		TimeUnit.SECONDS.sleep(5);
	    } catch (InterruptedException e) {
	    }

	    for (int i = 0; i < 15; i++) {
		try {
		    TimeUnit.MILLISECONDS.sleep(random.nextInt(2500));
		} catch (InterruptedException e) {
		}
		machine.broadCastTest(String.valueOf(i));
	    }
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
