package concurrency.temp.chapter18;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;


public class Cpu100 {
    private static final StampedLock lock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {
        Thread T1 = new Thread(() -> {
            lock.writeLock();
            //永远阻塞 , 不释放写锁
//            LockSupport.park();
        });
        T1.start();
        //保证s1阻塞
        Thread.sleep(100);

        Thread T2 = new Thread(() -> {
//            try {
//                lock.readLockInterruptibly();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
                lock.readLock();
        });
        T2.start();
        //保证s2阻塞
        Thread.sleep(100);
        //中断T2会使 T2所在CPU飙升
        T2.interrupt();
        T2.join();
    }

}
