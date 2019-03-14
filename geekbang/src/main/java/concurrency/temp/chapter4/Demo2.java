package concurrency.temp.chapter4;

/**
 * @author YunTianXiang
 * @Date 2019/3/14
 */
public class Demo2 {

	public static void main(String[] args) throws InterruptedException {

		Account2 account2 = new Account2();
		account2.balance = 1000;
		Thread threadA = new Thread(account2::withDraw);
		Thread threadB = new Thread(() -> {
			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("threadB look =" + account2.getBalance());
		});

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();

		System.out.println(account2.balance);
	}
}
