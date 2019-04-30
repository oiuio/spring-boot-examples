package concurrency.temp.chapter9;

public class InterruptDemo {

    /**
     * 当循环线程收到中断通知,退出循环,线程结束.
     */
    public static void main(String[] args) throws InterruptedException {

        Thread whileThread = new Thread(() -> {
            Thread th = Thread.currentThread();
            while (!th.isInterrupted()) {
                try {
                    Thread.sleep(3000);
                    System.out.println("whileThread sleep 3s");
                } catch (InterruptedException e) {
                    th.interrupt();//不加这个会发生什么呢?                   抛出异常后,会清空中断标志位. 需要重置
                    e.printStackTrace();
                }
            }
        });

        whileThread.start();
        System.out.println("whileThread start");
        Thread.sleep(1000);
        System.out.println("main Thread sleep 1s");
        System.out.println("interrupt whileThread");
        whileThread.interrupt();
        whileThread.join();
    }
}
