package concurrency.temp.chapter4;

/**
 * @author YunTianXiang
 * @Date 2019/3/14
 */
public class Account {

	protected final Object balLock = new Object();
	protected Integer balance;
	protected final Object pwLock = new Object();
	protected String password;

	//取款
	void withDraw(Integer amt) {
		synchronized (balLock) {
			if (this.balance > amt) {
				this.balance -= amt;
			}
		}
	}

	//查看余额
	Integer getBalance() {
		synchronized (balLock) {
			return balance;
		}
	}

	//更改密码
	void updatePassword(String pwd) {
//		synchronized (pwLock) {
			this.password = pwd;
//		}
	}

	//查看密码
	String getPassword() {
		synchronized (pwLock) {
			return password;
		}
	}
}
