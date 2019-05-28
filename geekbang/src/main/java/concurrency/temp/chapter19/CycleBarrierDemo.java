package concurrency.temp.chapter19;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CycleBarrierDemo {

    Vector<String> pos;
    Vector<String> dos;
    List<String> diff;

    Executor executor = Executors.newFixedThreadPool(1);
    final CyclicBarrier barrier = new CyclicBarrier(2, () -> executor.execute(this::check));


    void check() {
        String p = pos.remove(0);
        String d = dos.remove(0);
        diff = check(p, d);
        save(diff);
    }

    List<String> check(String p, String d) {
        return null;
    }

    void checkAll() {
        Thread T1 = new Thread(() -> {
            while (Boolean.TRUE) {
                try {
                    pos.add(getPOrders());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T1.start();
        Thread T2 = new Thread(() -> {
            while (Boolean.TRUE) {
                try {
                    dos.add(getDOrders());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T2.start();
    }
}
