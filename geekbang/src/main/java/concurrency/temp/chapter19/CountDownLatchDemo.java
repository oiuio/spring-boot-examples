package concurrency.temp.chapter19;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {
    /* 需求1
    {
        while (存在未对账订单) {
            //查询未对账订单
            pos = getPOrders();
            //查询派送订单
            dos = getDOrders();
            diff = check(pos, dos);
            save(diff);
        }
    }
     */


    //解决方案1: 查询未对账订单 查询派送订单 并行
    /*
    {
        while (存在未对账订单) {
           Thread T1 = new Thread(()->{
               pos = getPOrders();
           });
           Thread T2 = new Thread(()->{
               dos = getDOrders();
           });
           T1.start();
           T2.start();
           T1.join();
           T2.join();
           diff = check(pos,dos);
           save(diff);
        }
    }
     */

    //解决方案-改进1:复用线程,不每次创建新线程
    //无聊试了一下管程
    /*
    {
        Executor executor = Executors.newFixedThreadPool(2);
        //存在未对账订单
        final Lock lock = new ReentrantLock();
        final Condition isZero = lock.newCondition();
        while (Boolean.TRUE) {
            AtomicInteger count = new AtomicInteger(2);
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(() -> {
                lock.lock();
                try {
                    Thread.sleep(1000L);
                    pos = getPOrders();
                    System.out.println("getPOrders");
                    count.getAndDecrement();
                    isZero.signal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            });
            executor.execute(() -> {
                lock.lock();
                try {
                    Thread.sleep(1000L);
                    dos = getDOrders();
                    System.out.println("getDOrders");
                    count.getAndDecrement();
                    isZero.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            });
            System.out.println("lock");
            lock.lock();
            try {
                while (count.get() != 0) {
                    isZero.await();
                }
                //怎么知道线程池执行完了?
                List<String> diff = check(pos, dos);
                save(diff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
     */

    //解决方案-改进1:复用线程,不每次创建新线程
    //用CountDownLatch
    {
        Executor executor = Executors.newFixedThreadPool(2);
        //存在未对账订单
        while (Boolean.TRUE) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CountDownLatch latch = new CountDownLatch(2);
            executor.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    pos = getPOrders();
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            executor.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    dos = getDOrders();
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            try {
                latch.await();
                diff = check(pos, dos);
                save(diff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    private static List<String> pos = null;
    private static List<String> dos = null;
    private static List<String> diff = null;

    private static List<String> getPOrders() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    private static List<String> getDOrders() {
        List<String> list = new ArrayList<>();
        list.add("2");
        list.add("3");
        list.add("4");
        return list;
    }

    private static List<String> check(List<String> pos, List<String> dos) {
        List<String> pos1 = new ArrayList<>(pos);
        List<String> dos1 = new ArrayList<>(dos);

        List<String> result = new ArrayList<>();
        pos.removeAll(dos1);
        result.addAll(pos);
        dos.removeAll(pos1);
        result.addAll(dos);

        return result;
    }

    private void save(List<String> diff) {
        System.out.println(diff.toString());
    }

    public static void main(String[] args) {
        new CountDownLatchDemo();
    }

}
