package concurrency.temp.chapter18;

import java.util.concurrent.locks.StampedLock;

public class ThinkDemo {

    private double x, y;
    final StampedLock sl = new StampedLock();

    //隐含一个问题
    void moveIfAtOrigin(double newX, double newY) {
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    x = newX;
                    y = newY;
                    //stamp= ws;
                    break;
                } else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }

    }
}
