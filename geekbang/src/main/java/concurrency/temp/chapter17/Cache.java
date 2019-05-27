package concurrency.temp.chapter17;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache<K, V> {
    private final Map<K, V> m = new HashMap<>();
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock w = rwl.writeLock();
    private final Lock r = rwl.readLock();

    V get(K k) {

        r.lock();
        try {
            return m.get(k);
        } finally {
            r.unlock();
        }
    }

    V put(K k, V v) {
        w.lock();
        try {
            return m.put(k, v);
        } finally {
            w.unlock();
        }
    }

    //按需加载
    V get1(K k) {
        V v = null;
        r.lock();
        try {
            v = m.get(k);
        } finally {
            r.unlock();
        }
        if (v == null) {
            //可能会有多个线程走到这一步
            w.lock();
            //但只会有一个线程走到这一步
            try {
                //可能其他线程已经更新缓存 所以还需要再判断一下
                v = m.get(k);
                if (v == null) {
                    //查询v
                    v = m.put(k, v);
                }
            } finally {
                w.unlock();
            }
        }
        return v;
    }

    //错误: 锁升级
    V get2(K k) {
        V v = null;
        r.lock();
        try {
            v = m.get(k);
            if (v == null) {
                //ReadWriteLock 不支持锁升级, 再读锁没有释放时获取写锁 ,会导致写锁永久等待
                //但降级可以 -> 从写锁到读锁
                w.lock();
                try {
                    v = m.get(k);
                    if (v == null) {
                        //查询v
                        v = m.put(k, v);
                    }
                } finally {
                    w.unlock();
                }
            }
        } finally {
            r.unlock();
        }
        return v;
    }

}
