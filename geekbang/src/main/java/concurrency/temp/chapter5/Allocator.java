package concurrency.temp.chapter5;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YunTianXiang
 * @Date 2019/3/18
 */
public class Allocator {
	private List<Object> als = new ArrayList<>();

	synchronized boolean apply(Object from, Object to) {
		if (als.contains(from) || als.contains(to)) {
			return false;
		} else {
			als.add(from);
			als.add(to);
		}
		return true;
	}

	synchronized void free(Object from, Object to) {
		als.remove(from);
		als.remove(to);
	}
}
