package concurrency.temp.chapter16;

import java.util.Queue;

public class Temp {
}


 class TempSemaphore{
    //计数器
    int count;
    //等待队列
    Queue queue;

    TempSemaphore(int c){
        this.count =c;
    }

    void down(){
        this.count--;
        if (count<0){
            //加入等待队列 阻塞
        }
    }

    void up(){
        this.count++;
        if (this.count<=0){
            //移除队列中的某个线程 唤醒
        }
    }

}