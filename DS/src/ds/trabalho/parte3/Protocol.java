package ds.trabalho.parte3;

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
    static final String MSG_TABLE_ENTRY = "TABLE";
    static final String MSG_REQUEST_TABLE = "SEND_TABLE";

    /**
     * Standard commands that we can send to our machine
     */
    static final String CMD_REGISTER = "register";
    static final String CMD_SHOW_IP_TABLE = "ipTable";
    static final String CMD_PUSH = "push";
    static final String CMD_PULL = "pull";
    static final String CMD_PUSH_PULL = "pushpull";
    static final String CMD_SHOW_DIC = "dic";
    static final String CMD_HELP = "help";

    static final String GOODBYE = "GOODBYE";
    static final String ACK_GOODBYE = "ACK_GOODBYE";

    /**
     * /** Identifier
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
    public static void proccessMessage(Connection connection, String message) {
	if (message == null || connection == null) {
	    System.out.println("[ERROR] Protocol: invalid message:");
	    return;
	}

	String arr[] = message.split(":");
	System.out.println("[INFO] Protocol: Got message: " + arr[2]);

	if (arr.length < 3)
	    System.out.println("[ERROR] Protocol: Wrong format: " + message);

	switch (arr[2]) {
	case MSG_HELLO:
	    connection.setOtherMachineId(Integer.parseInt(arr[1]));
	    return;

	case MSG_TABLE_ENTRY:
	    Dictionary.addWord(arr[3]);
	    return;

	case MSG_REQUEST_TABLE:
	    System.out.println("[INFO] Protocol: Pull request from: "
		    + connection.getOtherMachineId());

	    for (String word : Dictionary.getDic()) {
		Protocol.sendMessage(connection, MSG_TABLE_ENTRY, word);
	    }

	    return;

	case GOODBYE:
	    connection.send(ACK_GOODBYE, true);
	    connection.close();
	    break;

	case ACK_GOODBYE:
	    connection.close();
	    break;

	default:
	    System.out.println(
		    "[ERROR] Protocol: unkown message format: " + message);
	    break;
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
    public static void sendMessage(Connection connection, String msgCode,
	    String msgValue) {

	switch (msgCode) {
	case MSG_HELLO:
	case MSG_TABLE_ENTRY:
	case MSG_REQUEST_TABLE:
	    connection.send(buildMessage(connection, msgCode, msgValue), false);
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
    public static void proccessCommand(String command) {
	String[] arr = command.split("\\(|\\)|,");

	System.out.println("[INFO] Protocol: Got command: " + arr[0]);

	Connection connection;

	switch (arr[0]) {
	case CMD_REGISTER:
	    curMachine.register(arr[1]);
	    return;

	case CMD_SHOW_IP_TABLE:
	    curMachine.showCurrentIpTable();
	    return;

	case CMD_PUSH:
	    if (arr.length < 2) {
		System.out
			.println("[ERROR] Protocol: Weird command: " + command);
		return;
	    }

	    connection = curMachine.findConnection(arr[1]);

	    if (connection == null) {
		System.out.println("[ERROR] Protocol: Could not find dest:");
		return;
	    }

	    else {
		for (String word : Dictionary.getDic()) {
		    Protocol.sendMessage(connection, MSG_TABLE_ENTRY, word);
		}
	    }

	    break;

	case CMD_PULL:
	    if (arr.length < 2) {
		System.out.println(
			"[ERROR] Protocol: Command does not contain ip or machine number");
		return;
	    }

	    connection = curMachine.findConnection(arr[1]);

	    if (connection == null) {
		System.out.println("[ERROR] Protocol: Could not find dest:");
		return;
	    }

	    Protocol.sendMessage(connection, MSG_REQUEST_TABLE, "");
	    break;

	case CMD_PUSH_PULL:
	    proccessCommand("push(" + arr[1] + ")");
	    proccessCommand("pull(" + arr[1] + ")");
	    return;

	case CMD_SHOW_DIC:
	    Dictionary.showDic();
	    return;

	case CMD_HELP:
	    System.out.printf("[INFO] Protocol: Help: %s: %s: %s: %s: %s:\n",
		    CMD_SHOW_IP_TABLE, CMD_PULL, CMD_PULL, CMD_PUSH_PULL,
		    CMD_SHOW_DIC);
	    break;
	default:
	    System.out.println("[ERROR] Protocol: Unkown command: " + command);
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
