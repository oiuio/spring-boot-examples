package concurrency.temp.chapter22;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo22 {

}

class MyThreadPool {
    //阻塞队列实现 生产者 - 消费者
    BlockingQueue<Runnable> workQueue;
    List<WorkerThread> threads = new ArrayList<>();

    MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        for (int idx = 0; idx < poolSize; idx++) {
            WorkerThread work = new WorkerThread();
            work.start();
            threads.add(work);
        }
    }

    void execute(Runnable command) {
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class WorkerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);
                    Runnable task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(2);
        MyThreadPool pool = new MyThreadPool(2, workQueue);
        pool.execute(() -> System.out.println("hello 1"));
        pool.execute(() -> System.out.println("hello 2"));
        pool.execute(() -> System.out.println("hello 3"));
        pool.execute(() -> System.out.println("hello 4"));
        pool.execute(() -> System.out.println("hello 5"));
    }


}

class ReNameThreadFactory implements ThreadFactory {
    //线程池编号
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    //线程编号
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    //线程组
    private final ThreadGroup group;
    //业务名称前缀
    private final String namePrefix;
    public ReNameThreadFactory(@NonNull String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-poolNumber:" + POOL_NUMBER.getAndIncrement() + "-threadNUMBER:";
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
    public static void main(String[] args) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(2);
        ReNameThreadFactory factory = new ReNameThreadFactory("papa");

        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(1, 2, 10, TimeUnit.SECONDS, workQueue, factory);
        for (int i = 0; i < 4; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.submit(() -> {
                System.out.println(executor.getPoolSize());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " | say hello");
            });
        }
    }
}
