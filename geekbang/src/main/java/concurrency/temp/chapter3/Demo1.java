package concurrency.temp.chapter3;

/**
 * @author YunTianXiang
 * @Date 2019/3/13
 */
public class Demo1 {
	//修饰非静态方法
	synchronized void foo() {
		//临界区
	}
	//修饰非静态方法相当于
//	synchronized(this) void foo() {
//		//临界区
//	}

	//修饰静态方法
	synchronized static void bar() {
		//临界区
	}
	//修饰静态方法相当于
//	synchronized(Demo1.class) static void bar() {
//		//临界区
//	}

	Object object = new Object();

	//修饰代码块
	void baz() {
		synchronized (object) {
			//临界区
		}
	}
}
