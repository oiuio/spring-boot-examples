package concurrency.temp.chapter5;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * @author YunTianXiang
 * @Date 2019/3/15
 */
@Slf4j
public class Demo1 {

	public static void main(String[] args) throws InterruptedException {

		Account1 accountA = new Account1();
		accountA.balance = 1000;
		Account1 accountB = new Account1();
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

		log.debug("");


	}


	
}
