package concurrency.temp.chapter15;


import java.util.Date;

/**
 * 模拟异步转同步
 */
public class AsyncDemo {

    private static volatile String MESSAGE = null;

    public static void main(String[] args) {
        System.out.println("send RPC");
        String result = sendRPC();
        System.out.println("receive RPC result = " + result);
    }


    //模拟发送RPC
    private static String sendRPC() {
        String result;
        handleMessage();//调用一下 让模拟的远程RPC返回 ..
        result = get();
        return result;
    }

    //模拟轮询获取返回结果
    private static String get() {
        while (MESSAGE == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waiting ..");
        }
        return MESSAGE;
    }

    //模拟RPC返回结果
    private static void receiveRPC(String message) {
        MESSAGE = message;
    }

    //模拟远程RPC处理了数据后返回
    private static void handleMessage() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                receiveRPC("hello caller now is " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

