package concurrency.temp.chapter5;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
public class Account3 {
	//单例
	protected int id;
	protected int balance;

	void transfer(Account3 target, int amt) {
		Account3 left = this;
		Account3 right = target;
		if (left.id > target.id) {
			left = target;
			right = this;
		}

		synchronized (left) {
			synchronized (right) {
				if (this.balance >= amt) {
					this.balance -= amt;
					target.balance += amt;
				}
			}
		}
	}
}
