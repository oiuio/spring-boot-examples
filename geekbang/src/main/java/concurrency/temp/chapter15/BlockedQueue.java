package concurrency.temp.chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 */
public class BlockedQueue<T> {

    final Lock lock = new ReentrantLock();
    //队列不满:允许入队
    final Condition notFull = lock.newCondition();
    //队列不空:允许出队
    final Condition notEmpty = lock.newCondition();
    //队列
    private List<T> array = new ArrayList<>();
    //最大长度为10
    private int max = 10;

    //入队
    void enq(T t) {
        lock.lock();
        try {
            while (array.size() == max) {
                System.out.println("ThreadA await");
                notFull.await();
            }
            System.out.println("ThreadA run");
            array.add(t);
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    //出队
    T deq() {
        T t = null;
        lock.lock();
        try {
            while (array.isEmpty()) {
                System.out.println("ThreadA await");
                notEmpty.await();
            }
            System.out.println("ThreadB run");
            Random random = new Random();
            int next = random.nextInt(array.size());
            t = array.get(next);
            array.remove(next);
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) throws InterruptedException {
        BlockedQueue<String> queue = new BlockedQueue<>();

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                queue.enq(i + "");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(queue.deq());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();


    }


}
