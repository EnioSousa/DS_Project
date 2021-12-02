package ds.trabalho.parte2;

public class Protocol {
    static final String MSG_REGISTER = "REGISTER";
    static final String MSG_HELLO = "HELLO";
    static final String MSG_TABLE = "TABLE";
    static final String MSG_SEND_TABLE = "SEND_TABLE";

    static final String CMD_REGISTER = "register";
    static final String CMD_SHOW_IP_TABLE = "ipTable";
    static final String CMD_PUSH = "push";
    static final String CMD_PULL = "pull";
    static final String CMD_PUSH_PULL = "pushpull";
    static final String CMD_SHOW_DIC = "dic";

    static final String FROM = "FROM";

    static Machine curMachine;

    public static void receive(Connection connection, String message) {
	if (message == null || connection == null)
	    return;

	String arr[] = message.split(":");

	if (arr.length < 3)
	    System.out.println("Wrong format: [" + message + "]");

	switch (arr[2]) {
	case MSG_HELLO:
	    connection.setOtherMachineId(Integer.parseInt(arr[3]));
	    System.out.println("Machine " + arr[1] + " says hello");
	    return;

	case MSG_TABLE:
	    curMachine.addWord(arr[3]);
	    return;

	case MSG_SEND_TABLE:
	    System.out.println(
		    "Pull request from: " + connection.getOtherMachineId());
	    curMachine.push(connection);
	    return;
	}
    }

    public static void send(Connection connection, String msgCode,
	    String msgValue) {

	switch (msgCode) {
	case MSG_HELLO:
	case MSG_TABLE:
	case MSG_SEND_TABLE:
	    connection.write(buildMessage(connection, msgCode, msgValue));
	    return;

	}
    }

    public static String buildMessage(Connection connection, String msgCode,
	    String msgValue) {
	return FROM + ":" + String.valueOf(connection.getMyMachineId()) + ":"
		+ msgCode + (msgValue == null ? "" : ":" + msgValue);
    }

    public static void doAction(String command) {
	String[] arr = command.split("\\(|\\)|,");

	switch (arr[0]) {
	case CMD_REGISTER:
	    // Here we check if we register with ip or ip and port number
	    // Since to test locally i can only do it with localhost as ip
	    if (arr.length > 2) {
		curMachine.register(arr[1], Integer.valueOf(arr[2]));
	    } else {
		curMachine.register(arr[1]);
	    }
	    return;

	case CMD_SHOW_IP_TABLE:
	    curMachine.showCurrentIpTable();
	    return;

	case CMD_PUSH:
	    if (arr.length < 2) {
		System.out.println(
			"Command does not contain ip or machine number");
		return;
	    }

	    // Check if we have ip or machine number
	    String[] str = arr[1].split("\\.");

	    if (str.length > 1) {
		curMachine.push(arr[1]);
	    } else {
		curMachine.push(Integer.parseInt(arr[1]));
	    }

	    return;

	case CMD_PULL:
	    if (arr.length < 2) {
		System.out.println(
			"Command does not contain ip or machine number");
		return;
	    }
	    // Check if we have ip or machine number
	    String[] str1 = arr[1].split("\\.");

	    if (str1.length > 1) {
		curMachine.pull(arr[1]);
	    } else {
		curMachine.pull(Integer.parseInt(arr[1]));
	    }

	    return;

	case CMD_PUSH_PULL:
	    doAction("push(" + arr[1]);
	    doAction("pull(" + arr[1]);
	    return;

	case CMD_SHOW_DIC:
	    curMachine.showCurrentDic();
	    return;

	default:
	    System.out.println("Unkown command [" + command + "]");
	}
    }

    public static void setCurMachine(Machine machine) {
	Protocol.curMachine = machine;
    }
}
