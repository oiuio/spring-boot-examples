package concurrency.temp.chapter5;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
public class Account2 {
	//单例
	protected int id;
	protected Allocator actr;
	protected int balance;

	void transfer(Account2 target, int amt) {
		while (!actr.apply(this, target)) ;
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
