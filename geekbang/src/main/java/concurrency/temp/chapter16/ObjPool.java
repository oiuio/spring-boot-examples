package concurrency.temp.chapter16;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class ObjPool<T, R> {

    final List<T> pool;
    final Semaphore sem;

    ObjPool(int size, T t) {
        pool = new Vector<T>() {

            private static final long serialVersionUID = -852628404552923914L;
        };
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }


    R exec(Function<T, R> func) {
        T t = null;
        try {
            sem.acquire();
            t = pool.remove(0);
            return func.apply(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.add(t);
            sem.release();
        }
        return null;
    }

    public static void main(String[] args) {
        ObjPool<Long, String> objPool = new ObjPool<>(10, 2L);
        System.out.println(objPool.exec(Object::toString));
    }

}
