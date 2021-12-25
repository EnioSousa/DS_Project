package ds.trabalho.parte3;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LamportClock {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static int time = 0;

    static Integer tick(int timeStamp) {
	Integer temp = null;
	lock.writeLock().lock();

	try {
	    time = Integer.max(timeStamp, time) + 1;
	    temp = time;
	} finally {
	    lock.writeLock().unlock();
	}

	return temp;
    }

    static Integer tick() {
	return tick(-1);
    }
}
