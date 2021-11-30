package ds.trabalho.parte1;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class is responsible for handling token related operations. Since we may have
 * a lot of threads trying to do operations on the token, this class uses lock
 * to make sure there is no concurrency
 * 
 * @author enio95
 *
 */
public class Token {
    static private boolean tokenOwn;
    static private boolean tokenLock;
    static private Integer tokenValue;

    static private ReadWriteLock lock = new ReentrantReadWriteLock();

    public Token(boolean tokenOwn, boolean tokenLock, Integer tokenValue) {
	super();
	setTokenOwn(tokenOwn);
	setTokenLock(tokenLock);
	setTokenValue(tokenValue);
    }

    public static void passToken() {
	passToken(Machine.findNextMachine());
    }

    /**
     * Pass the token
     * 
     * @param connection A connection with another machine
     */
    public static void passToken(Connection connection) {

	lock.writeLock().lock();

	try {
	    if (tokenOwn && !tokenLock && connection != null) {
		connection.send(
			Protocol.TOKEN + ":" + String.valueOf(tokenValue + 1));

		Token.tokenOwn = false;
	    }

	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Get a token from another machin
     * 
     * @param value The value of the token
     */
    public static void getToken(int value) {
	lock.writeLock().lock();

	try {
	    Token.tokenOwn = true;
	    Token.tokenValue = value;
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Checks if we own the token
     * 
     * @return true if we own the token otherwise false
     */
    public static boolean isTokenOwn() {
	lock.readLock().lock();

	boolean temp;

	try {
	    temp = tokenOwn;
	} finally {
	    lock.readLock().unlock();
	}

	return temp;
    }

    /**
     * Check if the machine has the token locked
     * 
     * @return true if the token is locked otherwise false
     */
    public static boolean isTokenLock() {
	lock.readLock().lock();

	boolean temp;

	try {
	    temp = tokenLock;
	} finally {
	    lock.readLock().unlock();
	}

	return temp;
    }

    /**
     * Get the current token value
     * 
     * @return the token value
     */
    public static int getTokenValue() {
	lock.readLock().lock();

	int temp;

	try {
	    temp = tokenValue;
	} finally {
	    lock.readLock().unlock();
	}

	return temp;
    }

    /**
     * Set the ownership of the token
     * 
     * @param tokenOwn true if we own the token, otherwise false
     */
    public static void setTokenOwn(boolean tokenOwn) {
	lock.writeLock().lock();

	try {
	    Token.tokenOwn = tokenOwn;
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Set the lock on the token
     * 
     * @param tokenLock true if we want to lock the token, otherwise false
     */
    public static void setTokenLock(boolean tokenLock) {
	lock.writeLock().lock();

	try {
	    Token.tokenLock = tokenLock;
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * set the token value
     * 
     * @param tokenValue the value for the token
     */
    public static void setTokenValue(Integer tokenValue) {
	lock.writeLock().lock();

	try {
	    Token.tokenValue = tokenValue;
	} finally {
	    lock.writeLock().unlock();
	}
    }

}
