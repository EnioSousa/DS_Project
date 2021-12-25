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
    static final String UNLOCK = "unlock()";
    /*
     * Unlock token
     */
    static final String LOCK = "lock()";
    /**
     * Command patter to print the machine state
     */
    static final String STATE = "state()";

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
	    connection.send(ACK_GOODBYE, true);
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
    static void processCommand(Machine machine, String command) {
	if (command == null) {
	    System.out.println("[ERROR] command is null");
	    return;
	}

	String[] arr = command.split(":");
	System.out.println("[INFO] Protocol: Got command: " + arr[0]);

	switch (arr[0]) {
	case LOCK:
	    Token.lockToken();
	    break;

	case UNLOCK:
	    Token.unlockToken();
	    break;

	case STATE:
	    machine.getMachineState();
	    break;

	default:
	    System.out.println("[ERROR] unkown command");
	    break;
	}
    }

}