package ds.trabalho.parte1;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Token {
    /**
     * if true we own the token
     */
    static private boolean tokenOwn = false;
    /**
     * if true we have a lock on the token
     */
    static private boolean tokenLock = false;
    /**
     * value of the token
     */
    static private Integer tokenValue = 0;
    /**
     * Lock to access the token values
     */
    static final private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructor will set the basic values for our token
     * 
     * @param tokenOwn   if true we own the token
     * @param tokenLock  if true we have a lock on the token
     * @param tokenValue value of the token
     */
    public Token(boolean tokenOwn, boolean tokenLock, Integer tokenValue) {
	lock.writeLock().lock();

	try {
	    Token.tokenLock = tokenLock;
	    Token.tokenOwn = tokenOwn;
	    Token.tokenValue = tokenValue;
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Tries to pass the token to another machine
     * 
     * @param connection The connection to the other machine
     */
    static void passToken(Connection connection) {
	if (connection == null)
	    return;

	lock.writeLock().lock();
	try {
	    if (tokenOwn && !tokenLock) {
		Protocol.sendMessage(connection, Protocol.TOKEN,
			String.valueOf(tokenValue + 1));

		System.out.println("[INFO] Token: pass token");
		tokenOwn = false;
	    }
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Get a token from another machine
     * 
     * @param value The value associated with the token received
     */
    static void getToken(int value) {
	lock.writeLock().lock();

	try {
	    tokenOwn = true;
	    tokenValue = value;
	    System.out.println("[INFO] Token: get token with value: "
		    + String.valueOf(value));
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Lock the token
     */
    static void lockToken() {
	lock.writeLock().lock();

	try {
	    tokenLock = true;
	    System.out.println("[INFO] Token: lock token:");
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Unlock the token
     */
    static void unlockToken() {
	lock.writeLock().lock();

	try {
	    tokenLock = false;
	    System.out.println("[INFO] Token: unlock token");
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Show current token state
     */
    static void showCurrentState() {
	lock.readLock().lock();

	try {
	    System.out.println(
		    "[INFO] Token: token value: " + tokenValue + ": Token own: "
			    + tokenOwn + ": Token lock: " + tokenLock);
	} finally {
	    lock.readLock().unlock();
	}
    }
}
