package ds.trabalho.parte2;

/**
 * Class is responsible for the definition of the procedures i.e. what to do if
 * we receive a certain command/message and how to properly send a message in
 * order to be recognised by all the machine in our network
 * 
 * @author enio95
 *
 */
public class Protocol {
    /**
     * Standard messages codes that we can exchange with other machines
     */
    static final String MSG_REGISTER = "REGISTER";
    static final String MSG_HELLO = "HELLO";
    static final String MSG_TABLE = "TABLE";
    static final String MSG_SEND_TABLE = "SEND_TABLE";

    /**
     * Standard commands that we can send to our machine
     */
    static final String CMD_REGISTER = "register";
    static final String CMD_SHOW_IP_TABLE = "ipTable";
    static final String CMD_PUSH = "push";
    static final String CMD_PULL = "pull";
    static final String CMD_PUSH_PULL = "pushpull";
    static final String CMD_SHOW_DIC = "dic";

    /**
     * Identifier
     */
    static final String FROM = "FROM";

    /**
     * The Current machine
     */
    static Machine curMachine;

    /**
     * Function is responsible for handling incoming messages from other machine
     * 
     * @param connection The connection that received the message
     * @param message    The message content
     */
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

    /**
     * Function is responsible for sending messages to a machine that is on the
     * other end of the connection
     * 
     * @param connection The connection with the other machine
     * @param msgCode    Standard message codes to talk with other machines
     * @param msgValue   Message value associated with message code
     */
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

    /**
     * Function builds a standard string for us to be able to talk with other
     * machines. Example: FROM:<our machine id>:<Message code>:<Message value>
     * 
     * @param connection connection with the other machine
     * @param msgCode    The message code we want to send
     * @param msgValue   The message content we want to send
     * @return a string
     */
    public static String buildMessage(Connection connection, String msgCode,
	    String msgValue) {
	return FROM + ":" + String.valueOf(connection.getMyMachineId()) + ":"
		+ msgCode + (msgValue == null ? "" : ":" + msgValue);
    }

    /**
     * Function responsible for handling commands from the user
     * 
     * @param command the command the user want us to execute
     */
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

	    if (arr[1].contains("l") || arr[1].contains(".")) {
		curMachine.pull(arr[1]);
	    } else {
		curMachine.pull(Integer.parseInt(arr[1]));
	    }

	    return;

	case CMD_PULL:
	    if (arr.length < 2) {
		System.out.println(
			"Command does not contain ip or machine number");
		return;
	    }

	    if (arr[1].contains("l") || arr[1].contains(".")) {
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

    /**
     * Set the current machine that has this protocol associated
     * 
     * @param machine The machine object that will use this protocol
     */
    public static void setCurMachine(Machine machine) {
	Protocol.curMachine = machine;
    }
}
