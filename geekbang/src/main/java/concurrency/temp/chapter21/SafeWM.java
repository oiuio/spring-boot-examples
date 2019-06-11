package concurrency.temp.chapter21;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.*;

public class SafeWM {
    class WMRange {
        final int upper;
        final int lower;

        WMRange(int upper, int lower) {
            this.upper = upper;
            this.lower = lower;
        }
    }

    final AtomicReference<WMRange> rf = new AtomicReference<>(new WMRange(0, 0));

    void setUpper(int v) {
        WMRange nr;
        WMRange or = rf.get();
        do {
            if (v < or.lower) {
                throw new IllegalArgumentException();
            }
            nr = new WMRange(v, or.lower);
        } while (!rf.compareAndSet(or, nr));
    }
}

class SimulatedCAS {
    volatile int count;

    void addOne() {
        int newValue;
        do {
            newValue = count + 1;//count = 9 ,newValue = 10
        } while (compare(count, cas(count, newValue)));//2
    }

    synchronized int cas(int expect, int newValue) {
        //expect = 9 ,newValue = 10
        //count = 10
        count = 10;
        int curValue = count;
        if (curValue == expect) {
            this.count = newValue;
        }
        //返回10
        return curValue;
    }

    public static void main(String[] args) {
        SimulatedCAS cas = new SimulatedCAS();
        cas.count = 9;
        cas.addOne();
    }

    boolean compare(int oldV, int newV) {
        System.out.println(oldV + ";" + newV);
        return oldV != cas(oldV, newV);
    }

    //原子类
    AtomicLong count1 = new AtomicLong(0);

    void add10K() {
        int idx = 0;
        while (idx++ < 10000) {
            count1.getAndIncrement();

        }
    }
}


class 原子化基本数据类型 {
    AtomicBoolean b;
    AtomicInteger i;
    AtomicLong l;

    void test() {
        int delta = 0;
        i.getAndIncrement();//i++
        i.getAndDecrement();//i--
        i.incrementAndGet();//++i
        i.decrementAndGet();//--i

        i.getAndAdd(delta);//当前值 +=delta 返回+=前的值
        i.addAndGet(delta);//当前值 +=delta 返回+=后的值
        i.compareAndSet(1, delta);//CAS 返回是否成功
        //新值可以通过传入func函数来计算
//        i.getAndUpdate();
//        i.updateAndGet();
//        i.getAndAccumulate();
//        i.accumulateAndGet();
    }
}

class 原子化的对象引用类型 {
    AtomicReference<List> r1;
    AtomicStampedReference<List> r2;
    AtomicMarkableReference<List> r3;

    void test() {
        List<String> s1 = new ArrayList<>();
        s1.add("1");
        List<String> s2 = new ArrayList<>();
        s2.add("2");
        r1 = new AtomicReference<>(s1);
        System.out.println(r1.get());
        List<String> s3 = new ArrayList<>();
        s3.add("3");
        r1.compareAndSet(s3,s2);
        System.out.println(r1.get());
    }

    public static void main(String[] args) {
        原子化的对象引用类型 r = new 原子化的对象引用类型();
        r.test();
    }

}

class 原子化数组 {
    AtomicIntegerArray array1;
    AtomicLongArray array2;
    AtomicReferenceArray array3;

    public static void main(String[] args) {

    }
}

class 原子化对象属性更新器 {
    AtomicIntegerFieldUpdater r1;
    AtomicLongFieldUpdater r2;
    AtomicReferenceFieldUpdater r3;
}

class 原子化的累加器 {
    DoubleAccumulator r1;
    DoubleAdder r2;
    LongAccumulator r3;
    LongAdder r4;
}


