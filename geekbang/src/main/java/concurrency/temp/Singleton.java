package concurrency.temp;

/**
 * @author YunTianXiang
 * @Date 2019/3/7
 */
public class Singleton {

	static Singleton instance;

	static Singleton getInstance() {
		if (instance == null) {
			synchronized (Singleton.class) {
				if (instance == null) {
					instance = new Singleton();
				}
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		System.out.println(getInstance());
	}

}
