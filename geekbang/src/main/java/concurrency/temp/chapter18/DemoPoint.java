package concurrency.temp.chapter18;

import java.util.concurrent.locks.StampedLock;

//官方示例修改
public class DemoPoint {

    private int x, y;
    final StampedLock sl = new StampedLock();

    int distanceFromOrigin() {
        //乐观读
        long stamp = sl.tryOptimisticRead();
        //读取局部变量,期间也可能被修改
        int curX = x, curY = y;
        //判断执行取操作期间是否存在写操作,如果存在则validate返回false
        if (sl.validate(stamp)) {
            //升级为悲观读锁
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                sl.unlockRead(stamp);
            }

        }

        return (int) Math.sqrt(curX * curX + curY * curY);
    }

}
