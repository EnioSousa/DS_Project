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
    static final String MSG_BROADCAST = "BROADCAST";
    static final String MSG_BLEATS = "BLEATS";
    /**
     * Shell commands
     */
    static final String CMD_REGISTER = "register";
    static final String CMD_STATE = "state";

    /**
     * 
     */
    static final String TIME_STAMP = "TIME_STAMP";

    /**
     * Synchronisation messages codes
     */
    static final String MSG_HELLO = "HELLO";
    static final String GOODBYE = "GOODBYE";

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

	if (arr.length < 6) {
	    System.out.println("[ERROR] Protocol: Wrong format: " + message);
	    return;
	}

	System.out.println("[INFO] Protocol: Got message: " + arr[2]);

	switch (arr[2]) {
	case MSG_HELLO:
	    connection.setOtherMachineId(Integer.parseInt(arr[1]));
	    connection.setTime(Integer.parseInt(arr[5]));
	    LamportClock.tick(Integer.parseInt(arr[5]));
	    Protocol.sendMessage(null, MSG_BLEATS, null);
	    break;

	case MSG_BROADCAST:
	    curMachine.deliver(getChatMessage(message));
	    connection.setTime(Integer.parseInt(arr[5]));
	    LamportClock.tick(Integer.parseInt(arr[5]));
	    Protocol.sendMessage(null, MSG_BLEATS, null);
	    break;

	case MSG_BLEATS:
	    connection.setTime(Integer.parseInt(arr[5]));
	    LamportClock.tick(Integer.parseInt(arr[5]));
	    break;

	case GOODBYE:
	    connection.close();
	    LamportClock.tick(Integer.parseInt(arr[5]));
	    Protocol.sendMessage(null, MSG_BLEATS, null);
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

	System.out.println("[INFO] Protocol: Sending message: " + msgCode);

	String string = buildMessage(msgCode, msgValue);

	switch (msgCode) {
	case MSG_HELLO:
	    connection.send(string, false);
	    break;

	case MSG_BROADCAST:
	    curMachine.deliver(getChatMessage(string));
	case MSG_BLEATS:
	    for (Connection con : curMachine.getConnections()) {
		con.send(string, false);
	    }
	    break;

	case GOODBYE:
	    if (connection == null) {
		for (Connection con : curMachine.getConnections()) {
		    con.send(string, true);
		    con.close();
		}
	    } else {
		connection.send(string, true);
		connection.close();
	    }
	    break;

	default:
	    System.out.println("[ERROR] Protocol: Unkown message code:");
	    break;
	}

    }

    private static ChatMessage getChatMessage(String message) {
	String arr[] = message.split(":");

	return new ChatMessage(arr[5], arr[1], arr[3]);
    }

    /**
     * Function builds a standard string for us to be able to talk with other
     * machines. This function also increments our lamport clock Example:
     * FROM:<our machine id>:<Message code>:<Message value>
     * 
     * @param connection connection with the other machine
     * @param msgCode    The message code we want to send
     * @param msgValue   The message content we want to send
     * @return a string A standard string that is recognisable in our network
     */
    public static String buildMessage(String msgCode, String msgValue) {
	return FROM + ":" + curMachine.getId() + ":" + msgCode
		+ (msgValue == null ? ":NONE" : ":" + msgValue) + ":"
		+ TIME_STAMP + ":" + LamportClock.tick();
    }

    /**
     * Function responsible for handling commands from the user
     * 
     * @param command the command the user want us to execute
     */
    public static void proccessCommand(String command) {
	String[] arr = command.split("\\(|\\)|\\(\\)");

	switch (arr[0]) {
	case CMD_STATE:
	    curMachine.getMachineState();
	    break;

	case CMD_REGISTER:
	    curMachine.register(arr[1]);
	    break;

	default:
	    System.out.println("[INFO] Protocol: Got chat message: ");

	    Protocol.sendMessage(null, MSG_BROADCAST, command);
	    break;
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
