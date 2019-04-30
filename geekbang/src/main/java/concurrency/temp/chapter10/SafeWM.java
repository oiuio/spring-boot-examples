package concurrency.temp.chapter10;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class SafeWM {

    private final AtomicLong upper = new AtomicLong(0);
    private final AtomicLong lower = new AtomicLong(0);

    void setUpper(long v) {
        if (v <= lower.get()) {
            return;
        }
        upper.set(v);
        printIfError();
    }

    void setLower(long v) {
        if (v >= upper.get()) {
            return;
        }
        lower.set(v);
        printIfError();
    }

    synchronized void printIfError() {
        if (upper.get() < lower.get()) {
            System.out.println("upper= " + upper.get() + " lower= " + lower.get());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SafeWM safeWM = new SafeWM();
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread threadA = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thread:" + Thread.currentThread().getName() + ",time: " + System.currentTimeMillis());
            Random random = new Random(100);
            for (int i = 0; i < 10000; i++) {
                safeWM.setLower(random.nextInt(100));
            }
        });

        Thread threadB = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thread:" + Thread.currentThread().getName() + ",time: " + System.currentTimeMillis());
            Random random = new Random(100);
            for (int i = 0; i < 10000; i++) {
                safeWM.setUpper(random.nextInt(100));
            }
        });

        threadB.start();
        threadA.start();
        threadA.join();
        threadB.join();

    }
}
