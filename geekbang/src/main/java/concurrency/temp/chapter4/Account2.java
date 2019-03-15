package concurrency.temp.chapter4;

/**
 * @author YunTianXiang
 * @Date 2019/3/14
 */
public class Account2 {

	protected final Object balLock = new Object();
	protected Integer balance;

	//取款
	synchronized void withDraw() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			balance -= 1;
		}
	}

	//查看余额 get 也要使用锁是重点
	synchronized Integer getBalance() {
//		synchronized (balLock) {
		return balance;
//		}
	}


}
