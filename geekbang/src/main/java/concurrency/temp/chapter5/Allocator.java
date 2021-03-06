package concurrency.temp.chapter5;

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

	synchronized boolean apply(Account2 from, Account2 to) {
		if (als.contains(from) || als.contains(to)) {
			if (from.id == 1) {
				intA++;
			} else {
				intB++;
			}
			return false;
		} else {
			als.add(from);
			als.add(to);
		}
		return true;
	}

	synchronized void free(Account2 from, Account2 to) {
		als.remove(from);
		als.remove(to);
	}
}
