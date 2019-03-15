package concurrency.temp.chapter5;

import lombok.extern.slf4j.Slf4j;

/**
 * @author YunTianXiang
 * @Date 2019/3/15
 */
public class Account1 {
	protected int balance;

	void transfer(Account1 account, int amt) {
		synchronized (this) {
			synchronized (account) {
				if (this.balance > amt) {
					balance -= amt;
					account.balance += amt;
				}
			}
		}
	}
}
