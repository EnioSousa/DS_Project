package ds.trabalho.parte2;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Dictionary {
    private HashSet<String> dic = new HashSet<>();

    public ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final String alphaSet = "abcdefghijklmnopqrstuvwxyz";
    private final SecureRandom rnd = new SecureRandom();

    public Dictionary() {
	addNewRandomWord(3000);
    }

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

    public void addWord(String str) {
	lock.writeLock().lock();

	try {
	    dic.add(str);
	} finally {
	    lock.writeLock().unlock();
	}
    }

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

    public String randomWord(int len) {
	StringBuilder sb = new StringBuilder(len);

	for (int i = 0; i < len; i++) {
	    sb.append(alphaSet.charAt(rnd.nextInt(alphaSet.length())));
	}

	return sb.toString();
    }

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

    public HashSet<String> getDic() {
	return dic;
    }
}
