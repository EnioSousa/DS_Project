package ds.trabalho.parte2;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class uses hashset to save words. This words can be manually added or
 * randomly generated.
 * 
 * @author enio95
 *
 */
public class Dictionary {
    /**
     * Data structure where we save our words
     */
    private HashSet<String> dic = new HashSet<>();
    /**
     * Lock used to access our data set.
     */
    public ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * Alphabet set used to generate random words
     */
    private final String alphaSet = "abcdefghijklmnopqrstuvwxyz";
    /**
     * Random generator
     */
    private final SecureRandom rnd = new SecureRandom();

    public Dictionary() {
	addNewRandomWord(3000);
    }

    /**
     * Add a single new word to our dictionary
     */
    public void addNewRandomWord() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		String str;

		do {
		    str = randomWord(rnd.nextInt(5) + 5);
		} while (containsWord(str));

		addWord(str);
	    }
	}).start();
    }

    /**
     * Add multiple word to our dictionary with an interval between them
     * 
     * @param time The interval between new words added
     */
    public void addNewRandomWord(long time) {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		String str;

		while (dic != null) {
		    do {
			str = randomWord(rnd.nextInt(5) + 5);
		    } while (containsWord(str));

		    addWord(str);

		    try {
			Thread.sleep(time);
		    } catch (InterruptedException e) {
			e.printStackTrace();
			return;
		    }
		}
	    }
	}).start();
    }

    /**
     * Add a single word to our dictionary. Method acquires a write lock
     * 
     * @param str word to add
     */
    public void addWord(String str) {
	lock.writeLock().lock();

	try {
	    dic.add(str);
	} finally {
	    lock.writeLock().unlock();
	}
    }

    /**
     * Checks if our dictionary contains a the given word
     * 
     * @param str word to check
     * @return true if its in the dictionary, otherwise false
     */
    public boolean containsWord(String str) {
	lock.readLock().lock();

	boolean contains;

	try {
	    contains = dic.contains(str);
	} finally {
	    lock.readLock().unlock();
	}

	return contains;
    }

    /**
     * Generate a random word a specific length
     * 
     * @param len length of the word
     * @return a random word
     */
    public String randomWord(int len) {
	StringBuilder sb = new StringBuilder(len);

	for (int i = 0; i < len; i++) {
	    sb.append(alphaSet.charAt(rnd.nextInt(alphaSet.length())));
	}

	return sb.toString();
    }

    /**
     * Print the current dictionary
     */
    public void showDic() {
	lock.readLock().lock();

	System.out.println("DIC START: SIZE:" + dic.size());

	try {
	    for (String str : dic) {
		System.out.println(str);
	    }
	} finally {
	    System.out.println("DIC END: SIZE: " + dic.size());
	    lock.readLock().unlock();
	}
    }

    /**
     * Get the current dictionary
     * 
     * @return the data structure that holds the words
     */
    public HashSet<String> getDic() {
	return dic;
    }
}
