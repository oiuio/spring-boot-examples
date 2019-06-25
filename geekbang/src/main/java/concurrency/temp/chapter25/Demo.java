package concurrency.temp.chapter25;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 25. CompletionService: 如何批量执行异步任务
 */
class Demo {

    static AtomicInteger r = new AtomicInteger(0);

    static Integer getPriceByS1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        return 1;
    }

    static Integer getPriceByS2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return 2;
    }

    static Integer getPriceByS3() throws InterruptedException {
        TimeUnit.SECONDS.sleep(8);
        return 3;
    }

    static void save(Integer i) {
        r.getAndAdd(i);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        创建CompletionService();
        实现Dubbo中ForkingCluster();
    }

    static void 创建CompletionService() throws InterruptedException, ExecutionException {
        //创建
//        ExecutorCompletionService(Executor executor) ;
//        ExecutorCompletionService(Executor executor, BlockingQueue<Future<V>> completionQueue)
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        cs.submit(Demo::getPriceByS1);
        cs.submit(Demo::getPriceByS2);
        cs.submit(Demo::getPriceByS3);
        for (int i = 0; i < 3; i++) {
            Integer r = cs.take().get();
            System.out.println(r);
            executor.execute(() -> save(r));
        }
        executor.shutdown();
        System.out.println(r);

        //其他接口
//        Future<V> submit(Callable<V> task); 提交任务
//        Future<V> submit(Runnable task, V result);  提交任务 ThreadPoolExecutor 中 submit
        //都是从阻塞队列获取并移除一个元素
//        Future<V> take() throws InterruptedException; 队列为空则阻塞
//        Future<V> poll(); 队列为空返回null
//        Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException; //支持超时的方式获取并移除阻塞队列头部第一个元素 超时返回null

    }

    //调用多个服务有一个成功返回就return 其他取消
    static void 实现Dubbo中ForkingCluster() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        List<Future<Integer>> futures = new ArrayList<>(3);
        futures.add(cs.submit(Demo::getPriceByS1));
        futures.add(cs.submit(Demo::getPriceByS2));
        futures.add(cs.submit(Demo::getPriceByS3));
        Integer r = 0;
        try {
            for (int i = 0; i < 3; i++) {
                r = cs.take().get();
                if (r != null) {
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            for (Future<Integer> future : futures) {
                future.cancel(true);
            }
        }
        executor.shutdown();
        System.out.println(r);
    }


}

class Test1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<Integer> f1 = executor.submit(Demo::getPriceByS1);
        Future<Integer> f2 = executor.submit(Demo::getPriceByS2);
        Future<Integer> f3 = executor.submit(Demo::getPriceByS3);
        Integer r1 = f1.get();
        System.out.println(r1);
        executor.execute(() -> Demo.save(r1));
        Integer r2 = f2.get();
        System.out.println(r2);
        executor.execute(() -> Demo.save(r2));
        Integer r3 = f3.get();
        System.out.println(r3);
        executor.execute(() -> Demo.save(r3));
        executor.shutdown();
        System.out.println(Demo.r);
//        executor.shutdown();
    }

}

class Test2 {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<Integer> f1 = executor.submit(Demo::getPriceByS1);
        Future<Integer> f2 = executor.submit(Demo::getPriceByS2);
        Future<Integer> f3 = executor.submit(Demo::getPriceByS3);
        executor.execute(() -> {
            try {
                bq.put(f1.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {
            try {
                bq.put(f2.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {
            try {
                bq.put(f3.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        for (int i = 0; i < 3; i++) {
            Integer r = bq.take();
            System.out.println(r);
            executor.execute(() -> Demo.save(r));
        }
        executor.shutdown();
        System.out.println(Demo.r);
    }

}

class TestAfterClass {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor =
                Executors.newFixedThreadPool(3);
        // 创建 CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        // 异步向电商 S1 询价
        cs.submit(Demo::getPriceByS1);
        // 异步向电商 S2 询价
        cs.submit(Demo::getPriceByS2);
        // 异步向电商 S3 询价
        cs.submit(Demo::getPriceByS3);
        // 将询价结果异步保存到数据库
        // 并计算最低报价
        AtomicReference<Integer> m = new AtomicReference<>(Integer.MAX_VALUE);
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                Integer r = null;
                try {
                    r = cs.take().get();
                } catch (Exception e) {
                }
                Demo.save(r);
//                m.getAndUpdate();
                m.set(Integer.min(m.get(), r));
                System.out.println(m);
            });
        }
        executor.shutdown();
        System.out.println(executor.isShutdown());
        System.out.println(executor.isTerminated());
        System.out.println(m);
    }
}