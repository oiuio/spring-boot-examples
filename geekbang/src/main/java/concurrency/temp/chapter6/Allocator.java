package concurrency.temp.chapter6;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
public class Allocator {
	protected List<Object> als = new ArrayList<>();

	protected int intA = 0;
	protected int intB = 0;

	synchronized void apply(Account from, Account to) {
		while (als.contains(from) || als.contains(to)) {
			if (from.id == 1) {
				intA++;
			} else {
				intB++;
			}
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			als.add(from);
			als.add(to);
	}

	synchronized void free(Account from, Account to) {
		als.remove(from);
		als.remove(to);
		notifyAll();
	}
}
