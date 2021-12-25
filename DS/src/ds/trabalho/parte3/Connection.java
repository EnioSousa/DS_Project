package ds.trabalho.parte3;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Connection {
    /**
     * Our machine id
     */
    private Integer myMachineId;
    /**
     * The other machine id
     */
    private Integer otherMachineId;
    /**
     * The socket to the other machine
     */
    private final Socket socket;
    /**
     * The address to the other machine
     */
    private final InetAddress address;
    /**
     * The object responsible for writing to the other machine
     */
    private WriteChannel write;
    /**
     * The object responsible for reading stuff from the other machine
     */
    private ReadChannel read;
    /**
     * Our machine "pointer"
     */
    private final Machine machine;
    /**
     * True is the connection is open, otherwise false
     */
    private boolean isOpen = true;

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int time = 0;

    /**
     * Creates the write and read channels, and sends an hello message with our
     * machine id
     * 
     * @param machine     Our machine "pointer"
     * @param socket      The socket to the other machine
     * @param myMachineId Our machine id
     */
    Connection(Machine machine, Socket socket, int myMachineId) {
	this.machine = machine;
	this.socket = socket;
	this.myMachineId = myMachineId;
	this.address = socket.getInetAddress();

	try {
	    this.write = new WriteChannel(this);
	} catch (Exception e) {
	    setOpen(false);
	    System.out.println(e);
	}

	try {
	    this.read = new ReadChannel(this);
	} catch (Exception e) {
	    setOpen(false);
	    write = null;
	    System.out.println(e);
	}
    }

    /**
     * Get our machine state
     */
    public void getMachineState() {
	machine.getMachineState();
    }

    /**
     * Check if our connection is open
     * 
     * @return boolean, true if the connection is alive
     */
    public boolean isOpen() {
	return isOpen;
    }

    /**
     * Set our connection the dead or alive
     * 
     * @param isOpen boolean, if true conencton is alive
     */
    public void setOpen(boolean isOpen) {
	this.isOpen = isOpen;
    }

    /**
     * Send a message to the other machine
     * 
     * @param message The mesasage to send
     * @param block   if true the call blocks
     */
    void send(String message, boolean block) {
	if (isOpen()) {
	    write.write(message, block);
	} else {
	    System.out.println("[ERROR] Connection: Connection is closed:");
	}
    }

    /**
     * Close the current connection. This method will also remove this
     * connection from the machine
     */
    void close() {
	try {
	    write.close();
	    read.close();
	} catch (Exception e) {
	}

	try {
	    socket.close();
	} catch (Exception e) {
	}

	setOpen(false);
	machine.remConnection(this);
    }

    public Integer getMyMachineId() {
	return myMachineId;
    }

    public Integer getOtherMachineId() {
	return otherMachineId;
    }

    public InetAddress getAddress() {
	return address;
    }

    public Socket getSocket() {
	return socket;
    }

    public void setMyMachineId(Integer myMachineId) {
	this.myMachineId = myMachineId;
    }

    public void setOtherMachineId(Integer otherMachineId) {
	this.otherMachineId = otherMachineId;
    }

    public void setTime(int value) {
	lock.writeLock().lock();
	;

	try {
	    time = value;
	} finally {
	    lock.writeLock().unlock();
	}
    }

    public Integer getTime() {
	Integer temp = null;

	lock.readLock().lock();

	try {
	    temp = time;
	} finally {
	    lock.readLock().unlock();
	}

	return temp;
    }
}
