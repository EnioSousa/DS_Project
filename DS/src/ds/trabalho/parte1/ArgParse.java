package ds.trabalho.parte1;

public class ArgParse {
    Integer id;
    Integer port;
    String[] ipSet;

    String[] argv;
    String[] options = { "--id", "--ipSet", "--listenPort" };

    public ArgParse(String[] argv) {
	this.argv = argv;

	if (checkArgs()) {
	    setValues();
	} else {
	    System.out.println(
		    "[ERROR] Usage: --id <id> --ipSet <ip,...,ip> --listenPort <port>");
	    return;
	}
    }

    private boolean checkArgs() {

	for (String option : options) {
	    if (findInArray(argv, option) < 0) {
		return false;
	    }
	}

	return true;
    }

    private void setValues() {
	id = Integer.parseInt(argv[findInArray(argv, "--id")]);

	if (findInArray(argv, "--ipSet") > 0)
	    ipSet = argv[findInArray(argv, "--ipSet")].split(",");

	port = Integer.parseInt(argv[findInArray(argv, "--listenPort")]);
    }

    private Integer findInArray(String[] argv, String str) {
	for (int i = 0; i < argv.length; i++) {
	    if (argv[i].equals(str))
		return i + 1;
	}

	return -1;
    }

    public Integer getId() {
	return id;
    }

    public Integer getPort() {
	return port;
    }

    public String[] getIpSet() {
	return ipSet;
    }

    public String getIp(int index) {
	return ipSet[index - 1];
    }
}