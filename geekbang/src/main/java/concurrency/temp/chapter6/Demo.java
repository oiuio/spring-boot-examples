package concurrency.temp.chapter6;

import lombok.extern.slf4j.Slf4j;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
@Slf4j
public class Demo {

	public static void main(String[] args) throws InterruptedException {
		Allocator allocator = new Allocator();

		Account accountA = new Account();
		accountA.id = 1;
		accountA.actr = allocator;
		accountA.balance = 1000;
		Account accountB = new Account();
		accountB.id = 2;
		accountB.actr = allocator;
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
		log.debug("AccountA failed = {}",allocator.intA);
		log.debug("AccountB failed = {}",allocator.intB);


		log.debug("accountA Balance = {}", accountA.balance);
		log.debug("accountB Balance = {}", accountB.balance);
	}

}
