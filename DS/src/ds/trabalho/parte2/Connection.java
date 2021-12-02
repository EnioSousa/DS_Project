package ds.trabalho.parte2;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private Integer myMachineId;
    private Integer otherMachineId;

    private WriteChannel writeChannel;
    private ReadChannel readChannel;

    /**
     * 
     * @param socket
     * @param myMachineId
     * @throws Exception
     */
    public Connection(Socket socket, int myMachineId) throws Exception {
	this.socket = socket;
	this.myMachineId = myMachineId;
	readChannel = new ReadChannel(this);
	writeChannel = new WriteChannel(this);

	Protocol.send(this, Protocol.MSG_HELLO, String.valueOf(myMachineId));
    }

    public void closeConnection() {
	try {
	    socket.getInputStream().close();
	    socket.getOutputStream().close();
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void write(String str) {
	if (writeChannel != null && !socket.isClosed())
	    writeChannel.write(str);
    }

    public Socket getSocket() {
	return socket;
    }

    public Integer getMyMachineId() {
	return myMachineId;
    }

    public Integer getOtherMachineId() {
	return otherMachineId;
    }

    public void setOtherMachineId(Integer otherMachineId) {
	this.otherMachineId = otherMachineId;
    }
}
