package concurrency.temp.chapter5;

import lombok.extern.slf4j.Slf4j;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
@Slf4j
public class Demo3 {

	public static void main(String[] args) throws InterruptedException {

		Account3 accountA = new Account3();
		accountA.id = 2;
		accountA.balance = 1000;
		Account3 accountB = new Account3();
		accountB.id = 1;
		accountB.balance = 1000;

		Thread threadA = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				accountA.transfer(accountB, 10);
			}
		});
		Thread threadB = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				accountB.transfer(accountA, 10);
			}
		});

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();

		log.debug("accountA Balance = {}", accountA.balance);
		log.debug("accountB Balance = {}", accountB.balance);
	}

}
