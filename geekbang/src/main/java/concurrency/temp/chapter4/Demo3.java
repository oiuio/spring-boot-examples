package concurrency.temp.chapter4;

/**
 * @author YunTianXiang
 * @Date 2019/3/15
 */
public class Demo3 {

	public static void main(String[] args) throws InterruptedException {
		Object object = new Object();
		Account account = new Account();
		account.balance = 1000;

		Account target = new Account();
		target.balance = 1000;

		Account third = new Account();
		third.balance = 1000;


		Thread threadA = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				account.transfer(target, 1);
			}
		});

		Thread threadB = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				target.transfer(third, 1);
			}
		});


		threadA.start();
		threadB.start();
		threadA.join();
		threadB.join();

		System.out.println("account = " + account.balance);
		System.out.println("target = " + target.balance);
		System.out.println("third = " + third.balance);

		//方式1 设定一个供用锁
		//方式2 设定class锁
	}
}
