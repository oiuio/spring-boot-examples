package concurrency.temp;

/**
 * @author YunTianXiang
 * @Date 2019/3/7
 */
public class Test1 {
	private long count = 0;

	private void add10K() {
		int idx = 0;
		while (idx++ < 10000) {
			count += 1;
		}
	}

	public static long calc() throws InterruptedException {
		final Test1 test1 = new Test1();
		Thread th1 = new Thread(test1::add10K);
		Thread th2 = new Thread(test1::add10K);
		th1.start();
		th2.start();
		th1.join();
		th2.join();
		return test1.count;
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println(calc());
	}
}
