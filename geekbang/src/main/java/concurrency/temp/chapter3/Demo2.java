package concurrency.temp.chapter3;

/**
 * @author YunTianXiang
 * @Date 2019/3/14
 */
public class Demo2 {

	public static void main(String[] args) throws InterruptedException {

		SafeCalc calc = new SafeCalc();
		Thread threadA = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				calc.add();
			}
		});
		Thread threadB = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				calc.add();
			}
		});
		Thread threadC = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				calc.add();
			}
		});
		Thread threadD = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				calc.add();
			}
		});
		threadA.start();
		threadB.start();
		threadC.start();
		threadD.start();

		threadA.join();
		threadB.join();
		threadC.join();
		threadD.join();

		System.out.println("end=" + calc.get());


	}

}


class SafeCalc {
	long value = 0l;

	long get() {
		return value;
	}

	 void add() {
		synchronized (this) {
			try {
				Thread.sleep(Math.round(5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			value = get() + 1;
		}
	}

}