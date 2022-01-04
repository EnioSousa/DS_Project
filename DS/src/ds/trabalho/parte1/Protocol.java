package ds.trabalho.parte1;

public class Protocol {
    /*
     * Received or send an hello message
     */
    static final String HELLO = "HELLO";
    /**
     * Close connection
     */
    static final String GOODBYE = "GOODBYE";
    /**
     * Acknowledge the closing connection
     */
    static final String ACK_GOODBYE = "ACK_GOODBYE";
    /**
     * Process a token pass
     */
    static final String TOKEN = "TOKEN";
    /*
     * Lock token
     */
    static final String UNLOCK = "unlock";
    /*
     * Unlock token
     */
    static final String LOCK = "lock";
    /**
     * Command patter to print the machine state
     */
    static final String STATE = "state";
    /**
     * Connect to another node
     */
    static final String REGISTER = "register";
    /**
     * Command to ask for help
     */
    static final String HELP = "Help";
    /**
     * The Current machine
     */
    static Machine curMachine;

    /**
     * Process a message received from another machine
     * 
     * @param connection The connection that received the message
     * @param message    The message received
     */
    static void proccessMessage(Connection connection, String message) {
	if (message == null) {
	    System.out.println("[ERROR] Protocol: invalid message:");
	    return;
	}

	String[] arr = message.split(":");
	System.out.println("[INFO] Protocol: Got message: " + arr[0]);

	switch (arr[0]) {
	case HELLO:
	    connection.setOtherMachineId(Integer.parseInt(arr[1]));
	    break;

	case TOKEN:
	    Token.getToken(Integer.parseInt(arr[1]));
	    break;

	case GOODBYE:
	    Protocol.sendMessage(connection, ACK_GOODBYE, null);
	    connection.close();
	    break;

	case ACK_GOODBYE:
	    connection.close();
	    break;

	default:
	    System.out.println(
		    "[ERROR] Protocol: unkown message pattern: " + message);
	    break;
	}
    }

    /**
     * Send a message to a connection with a request and a value associated
     * 
     * @param connection The connection to send the message
     * @param req        The request to send
     * @param value      The value associated with the request
     */
    static void sendMessage(Connection connection, String req, String value) {
	if (connection == null) {
	    System.out.println("[ERROR] connection is null");
	    return;
	}

	System.out.println("[INFO] Protocol: Trying to send message: " + req);

	switch (req) {
	case HELLO:
	    connection.send(HELLO + ":" + value, false);
	    break;

	case TOKEN:
	    connection.send(TOKEN + ":" + value, false);
	    break;

	case GOODBYE:
	    connection.send(GOODBYE + ":", false);
	    break;

	case ACK_GOODBYE:
	    connection.send(ACK_GOODBYE, true);
	    break;

	default:
	    System.out.println("[ERROR] unknown request");
	    break;
	}

    }

    /**
     * Process a command from our machine shell
     * 
     * @param machine Our machine
     * @param command The command we received from the shell
     */
    static void processCommand(String command) {
	if (command == null) {
	    System.out.println("[ERROR] command is null");
	    return;
	}
	String[] arr = command.split("\\(|\\)|\\(\\)");
	System.out.println("[INFO] Protocol: Got command: " + arr[0]);

	switch (arr[0]) {
	case LOCK:
	    Token.lockToken();
	    break;

	case UNLOCK:
	    Token.unlockToken();
	    break;

	case STATE:
	    curMachine.getMachineState();
	    break;

	case REGISTER:
	    curMachine.register(arr[1]);
	    return;

	case HELP:
	    StringBuilder strBuild = new StringBuilder();

	    strBuild.append(
		    "Help me pleasee!:\n command example:explanation\n");
	    strBuild.append("register(<machine name>):Connect to machine\n");
	    strBuild.append("unlock():Unlock token");
	    strBuild.append("lock():lock token");
	    strBuild.append("state():See machine state");

	    System.out.println("[STDOUT]:" + strBuild.toString());
	    break;
	default:
	    System.out.println("[ERROR] unkown command");
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