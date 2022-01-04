package ds.trabalho.parte3;

import java.util.Random;

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

	    Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
		    System.out.println("[STDOUT] Auto: Test synchronisation");

		    Random rnd = new Random();

		    while (machine.getIpTable().size() < 3) {
			try {
			    Thread.sleep(1000);
			} catch (Exception e) {
			    System.out.println("[ERROR] Auto: Sleep interrupt");
			}
		    }

		    System.out.println("[STDOUT] Auto: Test Start");

		    for (int i = 0; i < 15; i++) {

			machine.broadCastTest(
				String.valueOf(System.currentTimeMillis()));
			try {
			    Thread.sleep(rnd.nextInt(1000) + 250);
			} catch (Exception e) {
			    System.out.println("[ERROR] Auto: Sleep interrupt");
			}
		    }
		}
	    });

	    thread.start();
	}

    }
}
