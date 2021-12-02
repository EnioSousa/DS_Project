package ds.trabalho.parte2;

import java.io.IOException;
import java.net.Socket;

/**
 * Class is responsible for holding pertinent informantion about a connection.
 * This connection has a socket, write and read channel. The write and read
 * channels are object that create threads to read and write
 * 
 * @author enio95
 *
 */
public class Connection {
    /**
     * The socket connection with the other machine
     */
    private Socket socket;
    /**
     * Our machine ID
     */
    private Integer myMachineId;
    /**
     * The other machine ID
     */
    private Integer otherMachineId;
    /**
     * Object responsible for handling the writes
     */
    private WriteChannel writeChannel;
    /**
     * Object responsible for handling the reads
     */
    private ReadChannel readChannel;

    /**
     * COonstructor receives a socket and tries to initiate the read and write
     * channels. Once the object is created, it will try to send an hello
     * message to the other machine, if the channels were successfully created
     * 
     * @param socket      Socket connection with the other machine
     * @param myMachineId this machine id
     * @throws Exception
     */
    public Connection(Socket socket, int myMachineId) throws Exception {
	this.socket = socket;
	this.myMachineId = myMachineId;
	readChannel = new ReadChannel(this);
	writeChannel = new WriteChannel(this);

	Protocol.send(this, Protocol.MSG_HELLO, String.valueOf(myMachineId));
    }

    /**
     * Close connection will close the socket and the associated input and
     * output channels
     */
    public void closeConnection() {
	try {
	    socket.getInputStream().close();
	    socket.getOutputStream().close();
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Write a string to the socket.
     * 
     * @param str String to write
     */
    public void write(String str) {
	if (writeChannel != null && !socket.isClosed())
	    writeChannel.write(str);
    }

    /**
     * Get the socket associated with this connection
     * 
     * @return The associated socket
     */
    public Socket getSocket() {
	return socket;
    }

    /**
     * Get the id of our machine
     * 
     * @return
     */
    public Integer getMyMachineId() {
	return myMachineId;
    }

    /**
     * Get the id of the other machine. The machine that we are connected
     * 
     * @return
     */
    public Integer getOtherMachineId() {
	return otherMachineId;
    }

    /**
     * Set the Id of the other machine. This method is called when we receive an
     * hello message from the other machine. There are no security features
     * 
     * @param otherMachineId The other machine id
     */
    public void setOtherMachineId(Integer otherMachineId) {
	this.otherMachineId = otherMachineId;
    }
}
