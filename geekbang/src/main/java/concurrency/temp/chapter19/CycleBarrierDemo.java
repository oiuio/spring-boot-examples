package concurrency.temp.chapter19;

import java.util.ArrayList;
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
        List<String> pos1 = new ArrayList<>(pos);
        List<String> dos1 = new ArrayList<>(dos);

        List<String> result = new ArrayList<>();
        pos.removeAll(dos1);
        result.addAll(pos);
        dos.removeAll(pos1);
        result.addAll(dos);

        return result;
    }

    void checkAll() {
        Thread T1 = new Thread(() -> {
            while (Boolean.TRUE) {
                try {
                    pos.addAll(getPOrders());
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
                    dos.addAll(getDOrders());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T2.start();
    }
    private static List<String> getPOrders() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    private static List<String> getDOrders() {
        List<String> list = new ArrayList<>();
        list.add("2");
        list.add("3");
        list.add("4");
        return list;
    }

    private void save(List<String> diff) {
        System.out.println(diff.toString());
    }

    public static void main(String[] args) {
        new CycleBarrierDemo().checkAll();
    }

}
