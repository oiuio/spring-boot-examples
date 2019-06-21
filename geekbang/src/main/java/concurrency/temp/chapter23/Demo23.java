package concurrency.temp.chapter23;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class Demo23 {
}

class 获取任务执行结果 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        Runnable run = () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Callable<Integer> call = () -> {
            Thread.sleep(3000);
            return 1;
        };

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        Future<Integer> future = null;
//        Future future = executor.submit(run);
//        System.out.println("get");
//        System.out.println(future.get());
//        System.out.println("end");
//
//        System.out.println("=========================");
//        future = executor.submit(call);
//        System.out.println("get");
//        System.out.println(future.get());
//        System.out.println("end");

//        System.out.println("===========================");
//        future = executor.submit(call);
//        System.out.println("cancel");
//        System.out.println(future.isDone());
//        future.cancel(true);
//        future.get();
//        System.out.println(future.isCancelled());
//        System.out.println(future.isDone());
//        System.out.println("end");
//
//        System.out.println("===========================");
//        future = executor.submit(call);
//        System.out.println("get-timeout");
//        System.out.println(future.get(1, TimeUnit.SECONDS));;
//        System.out.println("end");

//        @Getter
//        @Setter
//        @ToString
//        class Result {
//            private int a = 1;
//            private int b = 1;
//        }
//
//        class Task implements Runnable {
//            Result r;
//
//            Task(Result r) {
//                this.r = r;
//            }
//
//            @Override
//            public void run() {
//                r.setA(2);
//                r.setB(3);
//            }
//        }
//        Result result = new Result();
//        Future<Result> future1 = executor.submit(new Task(result), result);
//        System.out.println(result);
//        System.out.println(future1.get());


        FutureTask<Integer> futureTask = new FutureTask<>(() -> 1 + 2);
        ExecutorService es = Executors.newCachedThreadPool();
        es.submit(futureTask);
        Integer result = futureTask.get();
        System.out.println(result);
    }

}

class FutureTask烧水泡茶 {
    static class T1Task implements Callable<String> {
        FutureTask<String> ft2;

        T1Task(FutureTask<String> ft2) {
            this.ft2 = ft2;
        }

        @Override
        public String call() throws Exception {
            System.out.println("T1: 洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T1: 烧开水...");
            TimeUnit.SECONDS.sleep(15);

            String tf = ft2.get();
            System.out.println("T1: 拿到茶叶" + tf);

            System.out.println("T1: 泡茶...");
            return "  上茶: " + tf;
        }
    }

    static class T2Task implements Callable<String>{

        @Override
        public String call() throws Exception {
            System.out.println("T2: 洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T2: 洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            System.out.println("T2: 拿茶叶");
            TimeUnit.SECONDS.sleep(1);
            return "龙井";
        }
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {


        FutureTask<String> ft2 = new FutureTask<>(new T2Task());
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));

        Thread t1 = new Thread(ft1);
        t1.start();
        Thread t2 = new Thread(ft2);
        t2.start();
        System.out.println(ft1.get());

    }
}

class CountDownLatch烧水泡茶{



    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<String> tea = new AtomicReference<>("");

        Thread t1 = new Thread(()->{
            try {
                System.out.println("T1 洗水壶...");
                TimeUnit.SECONDS.sleep(1);

                System.out.println("T1 烧开水");
                TimeUnit.SECONDS.sleep(15);

                countDownLatch.await();
                System.out.println("拿到茶叶" + tea);

                System.out.println("上茶");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(()->{
            try {
                System.out.println("T2 洗茶壶");
                TimeUnit.SECONDS.sleep(10);

                System.out.println("T2 洗茶杯");
                TimeUnit.SECONDS.sleep(10);

                System.out.println("T2 拿茶叶");
                TimeUnit.SECONDS.sleep(1);

                tea.set("龙井");
                countDownLatch.countDown();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(t1);
        executorService.execute(t2);



    }

}

