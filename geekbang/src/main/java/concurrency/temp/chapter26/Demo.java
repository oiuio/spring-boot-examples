package concurrency.temp.chapter26;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 26 Fork/Join: 单机版的MapReduce
 * 简单并行任务: 线程池 + Future 方案解决
 * 任务之间有聚合关系: CompletableFuture 解决
 * 批量的并行任务: CompletionService 解决
 * <p>
 * 分共,协作,互斥.
 * 线程池 , Future , CompletableFuture , CompletionService 都放到了分工里
 * 分治: 将一个负责的问题分解成多个相似的子问题 , 再把子问题分解成更小的子问题 , 直到子问题简单到可以直接求解
 * 算法中有分治算法(归并,快速排序,二分查找)
 * 大数据MapReduce背后思想也是分治
 * Java并发包提供了Fork/Join的并行计算框架用来支持分治任务模型
 */

class Demo {

}

/**
 * Fork对应分治任务模型的任务分解,Join对应结果合并
 * Fork/Join框架主要包含两部分,一部分分治任务线程池ForkJoinPool, 另一部分分治任务ForkJoinTask
 * 类似于ThreadPoolExecutor和Runnable的关系
 * ForkJoinTask是一个抽象类, 核心为fork()和join().
 * fork()会异步地执行一个子任务 , join()会阻塞当前线程等待子任务的执行结果
 * 子类: RecursiveAction和RecursiveTask. 看名字得值用递归来处理分治任务,还是两个抽象类, 定义了抽象方法compute()
 * 区别: RecursiveAction compute()没有返回值 , RecursiveTask的compute()有返回值
 * 调用分治任务线程 invoke()启动分治任务
 */
class ForkJoin的使用 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //创建分治任务线程池
        ForkJoinPool fjp = new ForkJoinPool(4);
        //创建分治任务
        Fibonacci fib = new Fibonacci(30);
        //启动
        Integer result = fjp.invoke(fib);
        System.out.println(result);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时: " + (endTime - startTime) / 1000);
    }

}

/**
 * e.g 计算斐波那契数列
 * 1 1 2 3 5 8 13 21 34
 * F(n) = F(n-1) + F(n-2)
 */
class Fibonacci extends RecursiveTask<Integer> {
    final int n;

    Fibonacci(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {

//        try {
//            TimeUnit.NANOSECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if (n <= 1)
            return n;
        Fibonacci f1 = new Fibonacci(n - 1);
        f1.fork();
        Fibonacci f2 = new Fibonacci(n - 2);
        return f2.compute() + f1.join();
    }
}

class 模拟MapReduce统计单词数量 {
    public static void main(String[] args) {
        String[] fc = {"hello world", "hello me", "hello fork", "hello join", "fork join in world"};
        ForkJoinPool fjp = new ForkJoinPool(3);

    }
}

class MR extends RecursiveTask<Map<String, Long>> {
    private String[] fc;
    private int start, end;

    MR(String[] fc, int fr, int to) {
        this.fc = fc;
        this.start = fr;
        this.end = to;
    }

    @Override
    protected Map<String, Long> compute() {
        return null;
    }

    //合并结果
    private Map<String, Long> merge(Map<String, Long> r1, Map<String, Long> r2) {
        Map<String, Long> result = new HashMap<>();
        result.putAll(r1);
        r2.forEach((k, v) -> {
            Long c = result.get(k);
            if (c != null) {
                result.put(k, c + v);
            } else {
                result.put(k, v);
            }
        });
        return result;
    }

    //统计单词数量
    private Map<String, Long> calc(String line) {
        Map<String, Long> result = new HashMap<>();
        String[] words = line.split("\\s+");
        for (String w : words) {
            Long v = result.get(w);
            if (v != null) {
                result.put(w, v + 1);
            } else {
                result.put(w, 1L);
            }
        }
        return result;
    }


}


