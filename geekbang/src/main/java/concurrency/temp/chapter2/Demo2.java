package concurrency.temp.chapter2;

/**
 * @author YunTianXiang
 * @Date 2019/3/14
 */
public class Demo2 {

	public static void main(String[] args) throws InterruptedException {

		JoinExample joinExample = new JoinExample();
		joinExample.v = 1;
		Thread threadA = new Thread(() -> {
			System.out.println(joinExample.v);
			joinExample.v = 2;
		});
		threadA.start();
		threadA.join();
		System.out.println(joinExample.v);
	}
}

class JoinExample {
	int v = 0;
}
