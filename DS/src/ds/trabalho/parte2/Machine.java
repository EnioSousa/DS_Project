package ds.trabalho.parte2;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Machine {
    private int id;
    private int listenPort;

    private Thread shell;
    private Thread listen;
    private List<Connection> connections;
    private List<InetAddress> ipTable;

    Dictionary dic;

    public Machine(int id, int listenPort) {
	super();
	this.id = id;
	this.listenPort = listenPort;
	this.connections = new ArrayList<>();
	this.ipTable = new ArrayList<>();
	this.dic = new Dictionary();

	Protocol.setCurMachine(this);

	shell = new Thread(new Runnable() {
	    @Override
	    public void run() {
		Scanner in = new Scanner(System.in);

		System.out.println("SHELL STARTED");

		while (in.hasNext()) {
		    Protocol.doAction(in.nextLine());
		}

		in.close();
		System.out.println("SHELL CLOSED");
	    }
	});
	shell.start();

	listen = new Thread(new Runnable() {
	    @Override
	    public void run() {
		System.out.println("START LISTENING ON PORT: " + listenPort);
		ServerSocket serverSocket = null;

		try {
		    serverSocket = new ServerSocket(listenPort);

		    while (true) {
			Socket socket = serverSocket.accept();

			if (!haveConnection(socket.getInetAddress())) {

			    addConnection(new Connection(socket, id));

			    ipTable.add(socket.getInetAddress());

			    System.out.println(
				    "ACCEPTED: " + socket.getInetAddress() + " "
					    + socket.getPort());
			} else {
			    System.out.println(
				    "REJECTED:" + socket.getInetAddress() + " "
					    + socket.getPort());
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    System.out.println("STOP LISTENING ON PORT: " + listenPort);
		    try {
			serverSocket.close();
		    } catch (IOException e) {
		    }
		}
	    }
	});
	listen.start();
    }

    public void attemptConnection(InetAddress ip, int port) {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		if (haveConnection(ip)) {
		    System.out.println("ALREADY CONNECTED: " + ip + " " + port);
		    return;
		}

		System.out.println("ATTEMPT: " + ip + " " + port);

		boolean tryAgain = true;

		while (tryAgain) {
		    try {
			Socket socket = new Socket(ip, port);
			System.out.println("SUCCESS: " + ip + " " + port);

			addConnection(new Connection(socket, id));

			ipTable.add(ip);

			tryAgain = false;
		    } catch (ConnectException e) {
			try {
			    Thread.sleep(3000);
			    System.out.println("RETRY: " + ip + " " + port);
			} catch (InterruptedException e1) {
			    System.err.println("Sleep interrupted:" + e1);
			}
		    } catch (Exception e) {
			e.printStackTrace();
			tryAgain = false;
			System.out.println("FAIL: " + ip + " " + port);
		    }
		}
	    }
	}).start();
    }

    public boolean haveConnection(InetAddress ip) {
	try {
	    if (ipTable.indexOf(ip) == -1
		    || ip.equals(InetAddress.getByName("localhost"))) {
		return false;
	    }
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}

	return true;
    }

    public void showCurrentIpTable() {
	System.out.println("Ip Table start:");
	int i = 0;
	for (InetAddress ip : ipTable) {
	    System.out.println("Entry: " + i + " " + ip);
	    i++;
	}
	System.out.println("Ip table end:");
    }

    public void showCurrentDic() {
	dic.showDic();
    }

    public void addConnection(Connection connection) {
	connections.add(connection);
    }

    public void remConnection(Connection connection) {
	connections.remove(connection);
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getListenPort() {
	return listenPort;
    }

    public void setListenPort(int listenPort) {
	this.listenPort = listenPort;
    }

    public void addWord(String word) {
	dic.addWord(word);
    }

    public void register(String ip) {
	register(ip, listenPort);
    }

    public void register(String ip, int port) {
	try {
	    register(InetAddress.getByName(ip), port);
	} catch (UnknownHostException e) {
	}
    }

    public void register(InetAddress ip, int port) {
	attemptConnection(ip, port);
    }

    public void push(String ip) {
	push(findConnection(ip));
    }

    public void push(int id) {
	push(findConnection(id));
    }

    public void push(Connection connection) {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		dic.lock.readLock().lock();

		try {
		    for (String string : dic.getDic()) {
			Protocol.send(connection, Protocol.MSG_TABLE, string);
		    }
		} finally {
		    System.out.println("Pushed has finished");
		    dic.lock.readLock().unlock();
		}
	    }
	}).start();

    }

    public void pull(String ip) {
	pull(findConnection(ip));
    }

    public void pull(int id) {
	pull(findConnection(id));
    }

    public void pull(Connection connection) {
	Protocol.send(connection, Protocol.MSG_SEND_TABLE, null);

    }

    public Connection findConnection(String ip) {
	for (Connection connection : connections) {
	    if (connection.getSocket().getInetAddress().getHostAddress()
		    .equals(ip))
		return connection;
	}

	return null;
    }

    public Connection findConnection(InetAddress ip) {
	for (Connection connection : connections) {
	    if (connection.getSocket().equals(ip))
		return connection;
	}

	return null;
    }

    public Connection findConnection(Integer id) {
	for (Connection connection : connections) {
	    if (connection.getOtherMachineId().equals(id))
		return connection;
	}

	return null;
    }

}
