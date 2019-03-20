package concurrency.temp.chapter6;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
public class Account {
    //单例
    protected int id;
    protected Allocator actr;
    protected int balance;

    void transfer(Account target, int amt) {
        actr.apply(this, target);
        try {
            synchronized (this) {
                synchronized (target) {
                    if (this.balance >= amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }

        } finally {
            actr.free(this, target);
        }
    }
}
