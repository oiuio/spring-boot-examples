package concurrency.temp.chapter24;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class Demo24 {
}

class 烧水泡茶 {

    public static void main(String[] args) {
        //无返回值 参数runnable
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1: 洗水壶");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T1: 烧开水");
            sleep(15, TimeUnit.SECONDS);
        });

        //有返回值 参数supplier
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("T2: 洗茶壶");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T2: 洗茶杯");
            sleep(2, TimeUnit.SECONDS);

            System.out.println("T2: 拿茶叶");
            sleep(1, TimeUnit.SECONDS);

            return "龙井";
        });

        CompletableFuture<String> f3 = f1.thenCombine(f2, (__, tf) -> {
            System.out.println("T1: 拿到茶叶:" + tf);
            System.out.println("T1: 泡茶...");
            return "上茶:" + tf;
        });
        System.out.println(f3.join());


    }

    private static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class 串行关系 {
    public static void main(String[] args) {

//        CompletionStage<R> thenApply(fn);
//        CompletionStage<R> thenApplyAsync(fn);
//        CompletionStage<Void> thenAccept(consumer);
//        CompletionStage<Void> thenAcceptAsync(consumer);
//        CompletionStage<Void> thenRun(action);
//        CompletionStage<Void> thenRunAsync(action);
//        CompletionStage<R> thenCompose(fn);
//        CompletionStage<R> thenComposeAsync(fn);


        CompletionStage completionStage = new CompletableFuture();
        Function fn = null;
        completionStage.thenApply(fn);
        completionStage.thenApplyAsync(fn);
        Consumer consumer = null;
        completionStage.thenAccept(consumer);
        completionStage.thenAcceptAsync(consumer);
        Runnable action = null;
        completionStage.thenRun(action);
        completionStage.thenRunAsync(action);
        completionStage.thenCompose(fn);
        completionStage.thenComposeAsync(fn);


        // Supplier 无参数 , 返回一个结果 get
        // Function 接受一个输入 , 返回一个结果 apply
        // Consumer 接受一个输入 , 无返回 accept

        CompletableFuture<String> f0 = CompletableFuture
                .supplyAsync(() -> "hello world")
                .thenApply(s -> s + " QQ")
                .thenApply(String::toUpperCase);
        System.out.println(f0.join());

        CompletableFuture<String> f1 = CompletableFuture
                .supplyAsync(() -> "hello")
                .thenCompose(string -> CompletableFuture.supplyAsync(() -> string + " world"));
        System.out.println(f1.join());
    }
}

class 描述AND汇聚关系 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletionStage<R> thenCombine(other, fn);
//        CompletionStage<R> thenCombineAsync(other, fn);
//        CompletionStage<Void> thenAcceptBoth(other, consumer);
//        CompletionStage<Void> thenAcceptBothAsync(other, consumer);
//        CompletionStage<Void> runAfterBoth(other, action);
//        CompletionStage<Void> runAfterBothAsync(other, action);


//        CompletableFuture<Void> completionStage = new CompletableFuture<>();
//        completionStage.thenRun(() -> {
//            System.out.println("second");
//        });
//        Thread.sleep(1000);
//        System.out.println("first");
//        System.out.println(completionStage.complete(null));




        CompletableFuture<String> combine = CompletableFuture.supplyAsync(() -> "大家好");
        combine = combine.thenCombine(CompletableFuture.supplyAsync(() -> "我是菜徐坤"), (v1, v2) -> {
            System.out.println(v1);
            System.out.println(v2);
            return "喜欢唱跳rap和打篮球";
        });
        System.out.println(combine.join());


        System.out.println("=====================================");
        CompletableFuture<Void> accept = CompletableFuture.supplyAsync(() -> {
            System.out.println("大家好");
            return null;
        });
        accept = accept.thenAcceptBoth(CompletableFuture.supplyAsync(() -> "我是菜徐坤"), (v1, v2) -> {
            System.out.println(v1);
            System.out.println(v2);
            System.out.println("喜欢唱跳rap和打篮球");
        });
        System.out.println(accept.join());

        System.out.println("=====================================");
        CompletableFuture<Void> run = CompletableFuture.supplyAsync(() -> {
            System.out.println("大家好");
            return null;
        });
        run = run.runAfterBoth(CompletableFuture.supplyAsync(() -> {
            System.out.println("我是菜徐坤");
            return "11111";
        }), () -> System.out.println("喜欢唱跳rap和打篮球"));
        System.out.println(run.join());

    }
}

class 描述OR汇聚关系{

    public static void main(String[] args) {
//        CompletionStage applyToEither(other, fn);
//        CompletionStage applyToEitherAsync(other, fn);
//        CompletionStage acceptEither(other, consumer);
//        CompletionStage acceptEitherAsync(other, consumer);
//        CompletionStage runAfterEither(other, action);
//        CompletionStage runAfterEitherAsync(other, action);



    }


}