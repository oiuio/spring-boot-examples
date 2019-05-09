package concurrency.temp.chapter15;


import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncDemo {

    private static volatile String MESSAGE = null;

    public static void main(String[] args) {
        System.out.println("send RPC");
        String result = sendRPC();
        System.out.println("receive RPC result = " + result);
    }

    //阻塞
    private static final Lock lock = new ReentrantLock();
    private static final Condition done = lock.newCondition();

    //模拟发送RPC
    private static String sendRPC() {
        lock.lock();
        String result;
        try {
            handleMessage();//调用一下 让模拟的远程RPC返回 ..
            result = get();
        } finally {
            lock.unlock();
        }

        return result;
    }

    //模拟获取返回结果
    private static String get() {
        lock.lock();
        try {
            if (MESSAGE == null) {
                System.out.println("waiting ..");
                //结果为空是等待
                done.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return MESSAGE;
    }

    //模拟RPC返回结果
    private static void receiveRPC(String message) {
        lock.lock();
        try {
            MESSAGE = message;
            //通知结果返回了
            done.signal();
        } finally {
            lock.unlock();
        }
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

