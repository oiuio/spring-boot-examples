package concurrency.temp.chapter14;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private int balance;
    private final Lock lock = new ReentrantLock();

    // 转账
    void transfer(Account tar, int amt) {
        while (true) {
            if (this.lock.tryLock()) {//活锁
                try {
                    if (tar.lock.tryLock()) {//活锁
                        try {
                            this.balance -= amt;
                            tar.balance += amt;
                        } finally {
                            tar.lock.unlock();
                        }
                    }//if
                } finally {
                    this.lock.unlock();
                }
            }//if
        }//while
    }//transfer
}
