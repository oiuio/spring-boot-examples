package concurrency.temp.chapter18;

import java.util.concurrent.locks.StampedLock;

public class Demo1 {

    final StampedLock sl = new StampedLock();

    //获取 / 释放悲观读锁
    {
        long stamp = sl.readLock();
        try {

        } finally {
            sl.unlockRead(stamp);
        }

    }

    //获取 / 释放写锁
    {
        long stamp = sl.writeLock();
        try {

        } finally {
            sl.unlockWrite(stamp);
        }
    }

}
