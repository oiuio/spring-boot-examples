package concurrency.temp.chapter2;

/**
 * @author YunTianXiang
 * @Date 2019/3/13
 */
public class Demo1 {

	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 100000; i++) {
			VolatileExample example = new VolatileExample();
			test(example);
		}


//		VolatileExample example = new VolatileExample();
//		example.setX(1);
//		Thread threadA = new Thread(() -> {
//			System.out.println(example.getX());
//		});
//		example.setX(3);
//		threadA.start();


	}

	public static void test(final VolatileExample example) throws InterruptedException {
		Thread threadA = new Thread(new Runnable() {
			public void run() {
				example.writer();
			}
		});
		Thread threadB = new Thread(new Runnable() {
			public void run() {
				example.reader();
			}
		});

		threadA.start();
		threadB.start();


	}
}


class VolatileExample {
	int x = 0;
	volatile boolean v = false;

	public void writer() {
		x = 42;
		v = true;
	}

	public void reader() {
		if (v == true) {
			System.out.println(x);
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}
}
