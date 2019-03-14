package concurrency.temp.chapter4;

/**
 * @author YunTianXiang
 * @Date 2019/3/14
 */
public class Demo1 {

	public static void main(String[] args) throws InterruptedException {
		Account account = new Account();
		account.balance = 1000;
		Thread threadA = new Thread(() -> {

			for (int i = 0; i < 100; i++) {
				account.withDraw(1);
			}
		});

		Thread threadB = new Thread(() -> {

			for (int i = 0; i < 100; i++) {
				account.withDraw(1);
			}
		});

		Thread threadC = new Thread(() -> {

			System.out.println(account.getBalance());
		});

		threadA.start();
		threadB.start();
		threadC.start();


		threadA.join();
		threadB.join();
		threadC.join();
	}
}
