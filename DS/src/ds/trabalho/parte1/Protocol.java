package ds.trabalho.parte1;

import java.util.Date;

/**
 * Protocol class on how to deal with shell commands and inter-machine messages
 * 
 * @author enio95
 *
 */
public class Protocol {
    /*
     * Send or Got the token
     */
    static final String TOKEN = "PASS_TOKEN";
    /*
     * Received or send an hello message
     */
    static final String HELLO = "HELLO";
    /*
     * Lock token
     */
    static final String UNLOCK_TOKEN = "UNLOCK";
    /*
     * Unlock token
     */
    static final String LOCK_TOKEN = "LOCK";
    /*
     * Time of last token received
     */
    static Date prevTime = new Date();

    /**
     * Function is responsible for coordinating the action of a command
     * 
     * @param connection connection that sent us the command
     * @param command    the command
     */
    public static void receive(Connection connection, String command) {
	if (command == null)
	    return;
	String[] arr = command.split(":");

	switch (arr[0]) {
	case HELLO:
	    connection.setMachineId(Integer.parseInt(arr[1]));
	    System.out.println("Connected with machine: " + arr[1]);
	    return;

	case TOKEN:
	    Token.getToken(Integer.parseInt(arr[1]));

	    Date curTime = new Date();

	    if (curTime.getTime() - prevTime.getTime() >= 2000) {
		System.out.println("Current token: " + Token.getTokenValue());
		prevTime = curTime;
	    }

	    Protocol.send(Machine.findNextMachine(), Protocol.TOKEN);
	    return;

	default:
	    return;
	}
    }

    /**
     * Method is responsible for sending a request to a given connection
     * 
     * @param connection connection to send the command/request
     * @param req        the request to send
     */
    public static void send(Connection connection, String req) {
	send(connection, req, null);
    }

    /**
     * Method is responsible for sending a request and a associated value to a
     * given connection
     * 
     * @param connection connection to send the request/command
     * @param req        command/request to send
     * @param value      value associated with the command
     */
    public static void send(Connection connection, String req, String value) {
	if (connection == null)
	    return;

	switch (req) {
	case TOKEN:
	    Token.passToken(connection);

	    return;

	case HELLO:
	    connection.send(HELLO + ":" + value);
	    return;
	default:
	    return;
	}
    }

    /**
     * Method is responsible for starting the execution of shell commands
     * 
     * @param command command to execute
     */
    public static void doAction(String command) {
	String[] arr = command.split(":");

	switch (arr[0]) {
	case UNLOCK_TOKEN:
	    Token.setTokenLock(false);
	    System.out.println("Token unloked");

	    Protocol.send(Machine.findNextMachine(), Protocol.TOKEN);
	    return;

	case LOCK_TOKEN:
	    Token.setTokenLock(true);
	    Integer temp = Token.getTokenValue();
	    System.out.println("Token lock: " + (temp == null ? "none" : temp));
	    return;

	default:
	    System.out.println("Unknown shell command [" + command + "]");
	    return;
	}
    }
}
