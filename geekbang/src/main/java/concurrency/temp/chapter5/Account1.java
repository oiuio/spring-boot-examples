package concurrency.temp.chapter5;


/**
 * @author YunTianXiang
 * @Date 2019/3/15
 */
public class Account1 {
	protected String name;
	protected int balance;

	void transfer(Account1 target, int amt) {
		synchronized (this) {
			synchronized (target) {
				if (this.balance > amt) {
					balance -= amt;
					target.balance += amt;
				} else {
					System.out.println(name + "没转成功" + ",钱=" + balance + ";" + target.name + "钱=" + target.balance);
				}
			}
		}
	}
}
