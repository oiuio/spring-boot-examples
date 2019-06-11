[toc]

# 并发编程

## 00.基础

## 01.可见性,原子性和有序性:并发编程Bug的源头
### 缓存导致的可见性问题
> 一个线程对共享变量的修改，另外一个线程能够立刻看到，我们称为可见性
* CPU直接操作的是缓存,在处理数据时,先把数据从内存读取大缓存,在缓存中处理完数据,再把数据写回内存
* 单核CPU中,所有线程在一个CPU上执行,同一个CPU中的缓存,不同的线程是可见的,一个线程修改了值V,另一个线程访问则一定是最新
* 多核CPU中,多个线程可能在不同的CPU上执行,不同的CPU缓存,对线程来说就是不可见的了,如果A-CPU中修改了值V,在B-CPU中的线程则不知道

* 样例:起多个线程同时修改一个变量值,值可能会出现意料之外的结果
  
### 线程切换带来的原子性问题
> 我们把一个或者多个操作在CPU执行的过程中不被中断的特性称为原子性
* 多进程的实现依赖了CPU的时间片轮转,每个进程执行一段时间则会让出CPU给其他进程执行
* 早期的操作系统是基于进程来调度CPU(因为不同进程不共享内存空间,切换需要重新映射内存地址),现代操作系统基于更轻量级的线程来调度
* CPU只能保证CPU指令级别是原子性的,但是一条高级语言通常需要几个CPU指令来完成
* 当线程A从内存取数据V加载到内存时,此时让出CPU由线程B执行,线程B也读取了数据V并修改了数据写回内存,由线程A继续执行,此时数据V还是原数据,写回内存时,覆盖了线程B的操作

* 样例:和上面其实差不多

### 编译优化带来的有序性问题
> 有序性指的是程序按照代码的先后顺序执行。编译器为了优化性能，有时会改变程序总语句的先后顺序

* 样例:双重锁
> 我们以为new执行是:
> 1.分配一块内存M 
> 2.在内存M上初始化Singleton对象 
> 3.将M的地址赋值给instance变量
> 实际优化后的执行是:
> 1.分配一块内存M 
> 2.将M的地址赋值给instance变量 
> 3.在内存M上初始化Singleton对象
> 线程A进入第二个判空条件，进行初始化时，发生了时间片切换，即使没有释放锁，线程B刚要进入第一个判空条件时，发现条件不成立，直接返回instance引用，不用去获取锁。

## 02.Java内存模型:看Java如何解决可见性和有序性问题
### 什么是Java内存模型
* 我们已经知道,导致可见性的原因是缓存,导致有序性的问题是编译优化,那么最直接的解决办法就是 **禁用缓存和编译优化** 
* Java内存模型规范了JVM如何提供按需禁用缓存和编译优化的方法.具体为 **volatile** , **synchronized** 和 **final** 关键字及六项 **Happens-Before规则**

### 使用volatile的困惑
* volatile关键字不是Java特产,C中也有,最原始的含义就是禁用CPU缓存
* 如果我们声明一个volatile变量,它表达的是: 告诉编译器,对这个变量的读写不能使用CPU缓存,必须从内存中读取或者写入

### Happens-Before规则
* 含义:前一个操作的结果对后续操作是可见的
* Happens-Before 约束了编译器的优化行为，虽允许编译器优化,但要求编译器优化后一定遵循该规则
* 与程序员相关的一共有六项,都是与可见性相关

#### 程序的顺序性规则
* 在一个线程中,按照程序顺序执行,前面的操作 Happens-Before 于后续的任意操作

#### volatile变量规则
* 对一个volatile变量的写操作 Happens-before 于后续对这个 volatile 变量的读操作

#### 传递性
* 如果A Happens-Before B, 且B Happens-Before C ,那么A Happens-Before C

#### 管程中锁的规则
* 指对一个锁的解锁 Happens-Before 于后续对这个锁的加锁
* 管程是一种通用的同部原语, 在Java中指的就是synchronized , synchronized是 Java对管程的实现

#### 线程start()规则
* 主线程A启动子线程B后 , 子线程B能看到主线程在启动子线程B前的操作
* 换句话: 线程A调用线程B的start()方法 , 那么该start()操作 Happens-Before 于线程B中的任意操作

#### 线程join()规则
* 主线程A等待子线程B完成(主线程A通过调用子线程B的join()方法实现),当子线程B完成后,主线程能够看到子线程的操作(对共享变量的操作)
* 换句话: 线程A中,调用线程B的join()并成功返回,那么B线程中的任意操作 Happens-Before 于该join()操作的返回 

### 被我们忽视的final
* 1.5版本前 final 的优化类似于双重锁的单利 , 构造函数的错误重排导致线程可能看到 final 的变量
* 1.5版本后 Java 内存模型对final类型变量重排序进行了约束. 现在只要我们构造函数没有逸出就不会出问题了

### 思考
> 有一个共享变量abc , 在一个线程里设置了abc = 3 , 有哪些方法可以使其他线程能够看到abc ==3

* thread join , volitail , synchronized , AtomicInteger

## 03.互斥锁(上): 解决原子性问题
* 原子性问题的源头是线程切换
* 同一时刻只有一个线程执行 称为 **互斥**
* 锁需要与锁保护的资源对应起来

### synchronized 关键字
* Java语言提供的 synchronized 关键字,是锁的一种实现,编译器会在 synchronized 修饰的方法或代码块前后自当加上 lock() 和 unlock()
* unlock()操作会使其他缓存的变量失效,需要重新从内存中加载变量值
* 当 synchronized 修饰非静态方法时 , 锁定的是当前实例对象 this
* 当 synchronized 修饰静态方法时 , 锁定的是当前类的 Class 对象
* 通常 , 受保护资源和锁之间的关联关系是N:1的关系 . 一个锁可以保护多个资源

## 04.互斥锁(下): 如何用一把锁保护多个资源
* 对于没有关联关系的资源 , 不同的资源用不同的锁保护. 用不同的锁对受保护资源进行精细化管理,能够提升性能,叫 **细粒度锁**
* 对于有关联关系的资源 , this锁只能保护自己的资源 , 不同的资源可以共享同一把锁 , 可以传入一把锁 或者使用 .class
* "原子性"的本质 是中间状态对外不可见 , 解决原子性问题 , 是要保证中间状态对外不可见.

### 思考
* 是否可以使用 this.balance 或者 this.passwword作为锁呢
* 答:不能使用可变对象作为锁

## 05.一不小心就死锁了,怎么办?
* 死锁: 一组互相竞争资源的线程因互相等待 , 导致"永久"阻塞的现象

### 预防死锁
> 死锁的四大条件 : 互斥 , 占用且等待(请求与保持) , 补可抢占(剥夺) , 循环等待
* 互斥: 我们用锁就是为了互斥所以没办法破坏
* 占用且等待: 一次性申请所有资源 , 就不用等待了
* 不可抢占: 占用部分资源的线程进一步申请其他资源时, 如果申请不到可以主动释放它占有的资源
* 循环等待: 可以按照资源的线性排序,申请时先申请序号小的,在申请序号大的,这样线性化后就不存在循环了

### 破坏占用且等待条件
* 为了做到同时申请,可以设置一个单例来进行管理,由一个人统一管理. 在进行操作时由这个单例统一锁定和解锁
  
### 破坏不可抢占条件
* 目前 synchronized 做不到 , 在synchronized申请资源时如果申请不到 , 线程直接进入阻塞状态

### 破坏循环等待条件
* 破坏这个条件,需要先队资源进行排序,然后按需申请资源,避免出现T1等待T2,T2等待T1

## 06.用"等待-通知"机制优化循环等待
* 等待-通知机制可以有多种实现方式, Java语言内置的 synchronized 配合 wait(),notify(),notifyAll()这三个方法可以轻松实现

> 当一个线程进入 synchronized 保护的临界区,其他线程只能进入(左边)等待队列里等待. **这个等待队列和互斥锁市一对一的关系,每个互斥锁都有自己独立的等待队列**
> 当调用wait()方法后, 当前线程会被阻塞,并且进入(右边)等待队列,这个等待队列也是 **互斥锁的等待队列** . 线程在进入等待队列的同时, **会释放持有的互斥锁** ,线程释放锁后,其他线程就有机会持有锁并进入临界区
> 当线程要求的条件满足时,通过java对象的notify()和notifyAll()方法,通知等待队列 **(互斥锁的等待队列)** 中的线程,告诉它条件 **曾经满足过**

* notify()只能保证在通知时间点,条件是满足的 , 执行时间点是否满足还得重新判断
* wait(),notify(),notifyAll()操作的都是互斥锁的等待对俄,如果 synchronized 锁的是this,那么就是this.wait(),notify()... 如果锁的是target那么就是 target.wait()...
* wait(),notify(),notifyAll()能被调用的前提是已经获取了相应的互斥锁,它们都是在 synchronized{}内部被调用的, 如果在外部调用会抛出IllegalMonitorStateException

### 尽量使用 notifyAll
* notify()会随机通知等待队列中的一个线程,而notifyAll会通知等待线程中的所有线程, 有可能使线程永远没机会被唤醒

### 思考: wait()和sleep()的区别
* wait会释放锁 sleep不会释放
* wait只能在同步方法和同步块中调用 sleep可在任何地方
* wait是Object的方法 sleep是Thread的方法
* wait无需捕获异常 , sleep需要
* sleep需要指定等待时间

## 07.安全性,活跃性以及性能问题
### 安全性
* 线程安全bug的源头: 原子性 , 可见性 , 有序性
* 存在会发生变化的共享数据,才会有线程安全的问题. 这种问题叫 **竞太条件** : 程序的执行结果依赖程序执行的顺序

### 活跃性
> 活跃性问题就是某个操作无法执行下去,除了死锁,还有 "活锁","饥饿"
* 活锁: 对应现实时间就是互相谦让对方先行 , 解决方案可以设置一个随机等待时间
* 饥饿: 线程因为无法访问所需资源而无法执行下去的情况.
> 在cpu繁忙时,优先级低的线程得到执行的机会小. 持有锁的线程如果执行时间过长,都会导致饥饿问题
* 饥饿解决方案: 1.保证资源充足 2.公平地分配资源 3.避免持有锁的线程长时间执行 .其中1.3场景有限,第二种用公平锁解决

### 性能问题
* 锁使用过度可能使串行化的范围过大,导致不能发挥多线程优势
* 阿姆达尔定律(Amdahl)代表处理器并行运算之后效率提升的能理 S = 1 / [(1-p)+p/n] n为cpu核数,p为并行百分比 1-p为串行百分比 如果n无穷大 串行比为5% ,加速比S的极限就是20 ,只能提升20倍性能
* JDK并发包有一堆东西,有很大一部分原因是为了提升在某个特定领域的性能

* 1. 使用锁会带来性能问题,最好的方案是使用无锁的算法和数据结构
> 相关技术: 本地存储(Thread Local Storage TLS),写入时复制(Copy-on-write),乐观锁等; Java并发包里的原子类也是一种无锁的数据结构,Disruptor是一个无锁的内存队列

* 2. 减少锁的持有时间.互斥锁的本质是将并行的程序串行化,增加并行度就一定要减少持有锁的时间.
> 例如细粒度锁,典型例子ConcurrentHashMap,使用了分段锁,还有读写锁,读是无锁的写才互斥

* 吞吐量:单位时间内能处理的请求数量. 越高性能越好
* 延迟:从发出请求到收到响应的时间. 越小性能越好.
* 并发量:能同时处理请求的数量. 一般来说并发量提高延迟也会增加,所以延迟一般基于并发量来说 例如并发量1000 延迟50ms

## 08.管程:并发编程的万能钥匙
* 管程:管理共享变量以及对共享变量的操作,让他们支持并发
* 管程和信号量是等价的,等价是指,管程能够实现信号量,信号量也能实现管程
* 管程的三种模型: Hasen模型,Hoare模型,MESA模型
![管程模型代码化的语义](https://static001.geekbang.org/resource/image/59/c4/592e33c4339c443728cdf82ab3d318c4.png)

### MESA模型
* 并发领域两大核心问题:
  * 互斥: 同一时刻只允许一个线程访问共享资源
  * 同步: 线程之间如何通信,协作

* 管程如何解决互斥问题: 
* 管程如何解决同步问题: 

## 09.Java线程(上):Java线程的生命周期

### 通用线程的生命周期: 
* **初始状态**,**可运行状态**,**运行状态**,**运行状态**,**休眠状态**和**终止状态**
![通用线程状态转换图-五态模型](https://static001.geekbang.org/resource/image/9b/e5/9bbc6fa7fb4d631484aa953626cf6ae5.png)

详细解释:
1. **初始状态**: 线程已被创建,但还不允许分配CPU执行. 这个状态是编程语言特有,代表它在编程语言层面被创建,在操作系统里还未创建
2. **可运行状态**: 线程可以分配CPU执行. 这个状态下,在操作系统中线程已被创建了
3. **运行状态**: 操作系统将CPU分配可一个处于可运行状态的线程,该线程就转换成了运行状态.
4. **休眠状态**: 运行状态的线程如果调用一个阻塞API(ex:以阻塞方式读文件)或者等待某个事件(ex:条件变量),线程就会转换到休眠状态,同时释放CPU使用权. 休眠状态的线程**永远没有机会获得CPU使用权**,当等待的事件出现了,它可以转变成**可运行状态**
5. **终止状态**: 线程执行完或者出现异常时,就会进入终止状态

### Java中线程的生命周期:
* NEW(初始化状态),RUNNABLE(可运行/运行状态),BLOCKED(阻塞状态),WAITING(无时限等待),TIMED_WAITING(有时限等待),TERMINATED(终止状态)
* 在操作系统层面,BLOCKED,WAITING,TIMED_WAITING 就是休眠状态,JAVA线程处于三种状态之一,就永远没有CPU使用权
![通用线程状态转换图-Java 中的线程状态转换图](https://static001.geekbang.org/resource/image/3f/8c/3f6c6bf95a6e8627bdf3cb621bbb7f8c.png)

####1. RUNNABLE 与 BLOCKED 的状态转换
只有在等待synchronized的隐式锁时,被阻塞的线程会转换恶的BLOCKED状态.
JVM不关心操作系统调度状态,它认为等待CPU与I/O没有区别,所以调用阻塞API并不会使线程发生状态改变.

####2. RUNNABLE 与 WAITING 的状态转换
场景一. 获得synchronized隐式锁的线程,调用无参数的Object.wait()
场景二. 调用无参数的Thread.join(). eg. Thread B中调用 Thread A.join,B:RUNNABLE->WAITING,当A执行完,B:WAITING->RUNNABLE
场景三. 调用LockSupport.park(),LockSupport,Java中并发包的锁都是基于其实现. 调用其方法,当前线程会阻塞,调用LockSupport.unpark(Thread)可以唤醒目标线程

####3. RUNNABLE 与 TIMED_WAITING 的状态转换
1. 调用带**超时参数**的 Thread.sleep(long millis)
2. 获得synchronized隐式锁的线程,调用带**超时参数**的Object.wait(long timeout)
3. 调用带**超时参数**的 Thread.join(long millis)
4. 调用带**超时参数**的 LockSupport.parkNanos(Object blocker,long deadline)
5. 调用带**超时参数**的 LockSupport.parkUntil(long deadLine)
WAITING 与 TIMED_WAITING 状态的区别,仅仅屎因为触发条件多了**超时参数**

####4. 从NEW 到 RUNNABLE 状态
Java刚创建出来的Thread对象就是New状态:继承Thread对象重写run(),实现Runnable()接口重写run()

####5. 从RUNNABLE 到 TERMINATED 状态
线程执行完后自动切换到 TERMINATED 状态, 在执行run() 抛出异常线程也会终止.
也可以手动调用 stop() interrupt()终止线程

##### stop() interrupt()区别:
* stop立刻杀死线程,如果线程持有ReentrantLock锁
* interrupt()紧紧是通知线程,线程有机会执行一些后续操作同时也可以无视通知. 通过异常,主动检测来接收通知
**interrupt()抛出异常后会清空中断标识,需要手动重置**

异常通知:
当线程A处于 WAITING ,TIMED_WAITING 状态时, 其他线程调用了A的 interrupt方法,会使A返回 RUNNABLE状态 , 同时A的代码会触发 InterruptException.转换到WAITING TIMED_WAITING的触发条件都是调用了 wait() sleep() join()方法 , 这些方法都会抛出InterruptException,这个异常的触发条件就是其他线程调用了该线程的interrupt
当线程A处于 RUNNABLE状态时 , 并且阻塞在 java.nio.channels.InterruptibleChannel时,其他线程调用了A的 interrupt(),A触发ClosedByInterruptException;阻塞在java.nio.channels.Selector上时,调用interrupt会使Selector立即返回
主动检测:
在线程处于RUNNABLE且没有阻塞在I/O操作上则依赖线程A的主动检测,通过调用自身的isInterrupt()来检测自身是否被中断

## 10.Java线程(中):创建多少线程才合适
运用多线程是为了**降低延迟,提高吞吐量** ,对应方法:**优化算法**,**发挥硬件性能极致** , 在并发领域就是为了 **提高I/O,CPU利用率**.
* 如果CPU和I/O设备利用率都很低 , 可以尝试增加线程来提高吞吐量
* 对于CPU密集型计算,理论上"线程数量=CPU核数"最合适,工程上一般会设置为"CPU核数+1",因为线程可能会因为某些原因阻塞.
* 对于I/O密集型计算,最佳线程数=1+(I/O耗时 / CPU 耗时), 意思为每个I/O等待中可以再处理几个CPU线程,多核系统中将结果再乘以CPU核数(一般使用逻辑核数)

* 具体最佳配置还是需要进行压测,apm工具精确到方法耗时 apache ab,apache JMeter

## 11.Java线程(下):为什么局部变量是线程安全的

* 方法是如何执行的
```java
int a = 7;
int [] b = fibonacci(a);
int [] c = b;
```
![方法的调用过程](https://static001.geekbang.org/resource/image/9b/1f/9bd881b545e1c67142486f6594dc9d1f.png)

* 当调用fibonacci()时,CPU先找到这个方法的地址,然后跳转到这个地址执行CPU代码,再执行完方法后,又会跳入下一个地址
* e.g 有三个方法A,B,C 调用关系为A->B->C 在运行时会构建出如下调用栈,每个方法在调用栈里都会有自己的独立空间,称为**栈帧**,每个栈帧有对应方法需要的参数和返回地址. 当调用方法时会创建新的栈帧,并压入调用栈,当方法返回时,对应的栈帧就会被自动弹出,栈帧和方法是同生共死的
![调用栈结构](https://static001.geekbang.org/resource/image/67/c7/674bb47feccbf55cf0b6acc5c92e4fc7.png)

* 局部变量的作用域是方法内部. 局部变量应该和方法同生共死. 局部变量就是放在了调用栈里
![保护局部变量的调用栈结构](https://static001.geekbang.org/resource/image/ec/9c/ece8c32d23e4777c370f594c97762a9c.png)

* 每个线程都有自己的独立的调用栈. 所以综上没有共享,没有并发问题.
* **线程封闭**: 单线程内访问数据. 方法里的局部变量没有共享,不会有并发问题,成为了一种解决并发问题的重药技术.
* 递归时,因为每调用一次方法就会创建新的栈帧,如果不控制栈大小就会出现栈溢出
* new 的对象在堆里,引用(句柄)在栈里

## 12.如何运用面向对象思想写好并发程序

* 封装共享变量: 将共享变量作为对象属性封装在内部,对所有公共方法制定并发访问策略. 对于不会发生变化的共享变量建议用**final**修饰
* 识别共享变量间的约束条件:共享变量之间的约束条件反应在代码里一般会有if语句,注意竞太条件
* 制定并发访问策略:
1. 避免共享: 避免共享的技术主要是利用线程本地存储以及为每个人物分配独立的线程
2. 不变模式: java中很少, 在其他领域中 Actor模式,CSP模式以及函数式编程的基础都是不变模式
3. 管程及其他同步类: 管程是java中的万能钥匙,但很多场景利用Java并发包提供的读写锁,并发容器等同步工具会更好
原则:
1. 优先使用成熟的工具类: 使用Java SDK 并发包里提供的丰富的工具类,避免重复造轮子 
2. 迫不得已使用低级的同步原语: synchronized , Lock , Semaphore 等
3. 避免过早优化: 安全第一个,出现性能瓶颈后再优化.

## 13.基础回顾

## 14.Lock和Condition(上):隐藏在并发包中的管程

* 并发编程的核心问题: **互斥** 与 **同步**
* JavaSDK 并发包通过 **Lock** 和 **Condition** 两个接口来实现管程 . **Lock解决互斥问题 , Condition解决同步问题**

死锁问题中 synchronized 不能解决 **破坏不可抢占条件** 方案 , 当 synchronized 申请资源时,如果申请不到线程则直接阻塞, 也不会释放已占有资源
如果重新设计锁来解决问题又三种方案:
1. 能够相应中断. 当进入阻塞状态时,可以发送中断相应唤醒线程并让他释放资源
2. 支持超时. 在一定时间内没有获取锁,不是进入阻塞状态而是返回错误,并释放资源
3. 非阻塞地获取锁. 当获取锁失败时,并不进入阻塞状态耳式直接返回,并释放锁

Lock接口中体现如下
```java
//支持中断的api
void lockInterruptibly() throw InterruptException;
//支持超时的api
boolean tryLock(long time,TimeUnit unit)  throw InterruptException;
//支持非阻塞获取锁的api
boolean tryLock();
```
保证可见性: ReentrantLock持有一个volatile变量state 在lock与unlock时分别进行操作,对value操作在中间,根据顺序性,volatile原则,传递性原则保证不同线程的可见性

### 可重入锁
可重入锁: 线程可以重复获取同一把锁
可重入函数: 多个线程可以同时调用该函数. 每个线程都能得到正确结果, 同时在一个线程内支持线程切换

### 公平锁与非公平锁
在使用ReentrantLock时,它提供两个两个构造函数 一个是无参 一个是传入fair参数 , fair代表锁的公平策略,true为构造公平锁,false则构造非公平锁
公平锁:等待队列中,谁等待的时间长就唤醒谁
非公平锁:不提供公平保证

### 用锁的最佳时间
1. 永远只在更新对象的成员变量时加锁
2. 永远旨在访问可变的成员变量时加锁
3. 永远不在调用其他对象的方法时加锁


## 15.Lock和Condition(下):Dubbo如何用管程实现异步转同步

* 同步与异步: 调用方是否需要等待结果,同步:是,异步:否
* Lock,Condition实现的管程线程的等待与通知: await(),signal(),signalAll()
* synchronized的线程的等待与通知: wait(),notify(),notifyAll()

## 16.Semaphore: 如何快速实现一个限流器
* 普遍翻译为:信号量 , 类似红绿灯
* 模型: 一个计数器 , 一个等待队列 , 三个方法{init,down,up} 保证为原子性操作
 * init:设置计数器初始值
 * down:计数器减1,若值小于0,当前线程被阻塞,否则继续执行
 * up:计数器加1,若值小于等于0,则唤醒等待队列中的一个线程,并将其从等待队列中移除
 * 信号量中down,up最早成为P,V操作,信号量模型也被成为PV原语,JDK中对应acquire和release
* Semaphore: acquire() release()

## 17.ReadWriteLock: 如何快速实现一个完备的缓存
* 适用于:读多写少
* 读写锁三条基本原则
 * 允许多个线程同时读共享变量
 * 只允许一个线程写共享变量
 * 如果一个线程正在执行写操作 , 此时禁止线程读共享变量
 
* 写锁能降级程都锁, 但读锁不能升级为写锁

* ReadWriteLock 是一个接口, 可以使用ReentrantReadWriteLock
 * 获取写锁的前提是读写锁都未被占用
 * 获取读锁的前提是写锁未被占用 (写锁与读锁互斥)
 * 申请写锁不中断其他线程申请读锁
 * 公平锁如果有写申请,能禁止读锁
 
# 18.StampedLock: 比读写锁更快的锁
* 支持三种模式: 写锁,悲观读锁,乐观读(无锁) ; 写锁与读锁互斥,只允许一个线程读锁
* 在写锁与悲观读锁加锁成功后会返回stamped解锁时需要在传入
* 与读写锁不同,在乐观读时,允许一个线程获取写锁
* 注意:
 * StampedLock是ReadWriteLock的子集,没有Reentrant不支持重入.
 * 悲观读锁,写锁不支持条件变量
 * 如果线程阻塞在readLock()或writeLock()时,调用阻塞线程的interrupt()方法会使cpu飙升到100%
 * 使用StampedLock一定不要调用中断操作,如果需要要使用可中断的readLockInterruptibly()和writeLockInterruptibly();
 
# 19.CountDownLatch和CycleBarrier: 让多线程步调一致

* CountDownLatch: 解决一个线程等待多个线程的场景
* CycleBarrier: 解决一组线程互相等待 , 计数器自动重置可重复利用
 * 回调函数执行再一个回合里最后执行await()的线程上,同步调用回调函数,执行完后才开始第二回合. 回调函数另开线程可以异步

# 20.并发容器: 都有哪些"坑"需要填
## 同步容器注意事项
* Java主要容器分为: List Map Set Queue 四大类,但并不是所有容器都是线程安全的
* ArrayList HashMap 非线程安全
* 坑1: **组合操作存在竞太条件问题** , 即便每个操作都能保证原子性 , 也不能保证组合操作的原子性
* 坑2: **用迭代器遍历容器** , 迭代中对元素进行操作
* 基于 **synchronized** 同步关键字实现的容器被称为 **同步容器**
* 还有有: Vector Stack Hashtable

## 并发容器及其注意事项
* 1.5版本前主要都是同步容器,1.5之后提供了许多并发容器,依旧为 List Map Set Queue 四大类
![并发容器关系图](https://static001.geekbang.org/resource/image/a2/1d/a20efe788caf4f07a4ad027639c80b1d.png)

### List
* 只有一个实现类:CopyOnWriteArrayList. 写入时会将共享变量复制一分
 * 读取遍历都在原数组A上执行,在写入时会复制一分数组B在B上进行写入,随后再将A指向B
 * 坑:
  * 使用场景:读多写少,且能容忍读写的短暂不一致
  * 迭代器是只读的,不支持增删改. 因为迭代器遍历的仅仅是快照
  
### Map
* 实现接口: ConcurrentHashMap 和 ConcurrentSkipListMap 
* ConcurrentHashMap 的 key 是 **无序** 的 , ConcurrentSkipListMap 的 key 是 **有序** 的
* key 与 value 都不能为空 ,否则会 NullPointerException
* ConcurrentSkipListMap 跳表. 跳表的插入,删除,查询操作平均时间复杂度为 O(log n)理论上和并发线程数无关, 在并发程度非常高的情况下可以替换 ConcurrentHashMap 试试
![Map相关的实现对于key和value的要求](https://static001.geekbang.org/resource/image/a2/1d/a20efe788caf4f07a4ad027639c80b1d.png)

### Set
* 实现接口: CopyOnWriteArraySet 和 ConcurrentSkipListSet 描述与之前名字相同的类似

### Queue
* 分类
 * 阻塞与非阻塞: 队列已满,入队操作阻塞;队列为空,出队操作阻塞;Blocking 关键字标识
 * 单端与双端: 单端-队尾入队,队首出队; 双端-队尾队首都可入队出队; 单端-Queue标识,双端-Deque标识

* 单端阻塞队列
 * 内部一般持有一个队列.
 * ArrayBlockingQueue: 队列是数组.
 * LinkedBlockingQueue: 队列是链表.
 * SynchronousQueue: 不持有队列, 生产者线程入队操作必须等待消费者线程出队操作
 * LinkedTransferQueue: 融合 LinkedBlockingQueue 和 SynchronousQueue 功能,性能比 LinkedBlockingQueue 更好
 * PriorityBlockingQueue: 支持按优先级出队
 * DelayQueue: 支持延时出队

* 双端阻塞队列
 * LinkedBlockingDeque
 
* 单端非阻塞队列
 * ConcurrentLinkedQueue
* 双端非阻塞队列 
 * ConcurrentLinkedDeque

* 只有 ArrayBlockingQueue LinkedBlockingQueue 支持有界, 在使用其他队列时需要充分考虑是否存在OOM 的隐患

## 21 原子类: 无锁工具类的典范
* 无锁方案不需要加锁解锁, 不会进入阻塞状态没有线程切换,性能好
* 方案: 
 * CPU提供了CAS指令(Compare and Swap) 保证操作原子性. 
 * 使用时需要传入一个期望值 expect 和写入的新值 newValue, 当当前值与expect相等时,才会将当前值更新为newValue,否则返回当前值,重新比较
 * 使用CAS解决并发问题一般会伴随自旋(循环尝试)
 * CAS会有ABA的问题, 即A变成了B再变成A,判断不出是否被更新过
```java
    void addOne() {
        int newValue;
        do {
            newValue = count + 1;
        } while (compare(count, cas(count, newValue)));//2
    }

    synchronized int cas(int expect, int newValue) {
        count = 10;
        int curValue = count;
        if (curValue == expect) {
            this.count = newValue;
        }
        return curValue;
    }
```
* Java实现原子化
 * compareAndSet方法返回是否更新成功
 * 底层是native方法
```java
public final long getAndAddLong(
  Object o, long offset, long delta){
  long v;
  do {
    // 读取内存中的值
    v = getLongVolatile(o, offset);
  } while (!compareAndSwapLong(
      o, offset, v, v + delta));
  return v;
}
// 原子性地将变量更新为 x
// 条件是内存中的值等于 expected
// 更新成功则返回 true
native boolean compareAndSwapLong(
  Object o, long offset, 
  long expected,
  long x);

```
### 原子类概览
* 主要分为: 原子化的基本数据类型 , 原子化的对象引用类型 , 原子化数组 , 原子化属性更新器 , 原子化的累加器
![原子类组成该蓝图](https://static001.geekbang.org/resource/image/00/4a/007a32583fbf519469462fe61805eb4a.png)

* 原子化的基本数据类型
 * AtomicBoolean AtomicInteger AtomicLong
 * getAndIncrement i++
 * getAndDecrement i--
 * incrementAndGet ++i
 * decrementAndGet --i
 * getAndAdd(delta) 返回+=前的值
 * addAndGet(delta) 返回+=后的值
 * compareAndSet(expect,update) 返回是否成功
 * getAndUpdate(func)
 * updateAndGet(func)
 * getAndAccumulate(func)
 * accumulateAndGet(func)
* 原子化的对象引用类型
 * AtomicReference 方法与基本数据类型相似
 * AtomicStampedReference 解决ABA问题,添加了版本号
 * AtomicMarkableReference 解决ABA问题,版本号->bool值
* 原子化数组
 * AtomicIntegerArray AtomicLongArray AtomicReferenceArray: 原子化更新数组里每一个元素 , 方法多了一个数组的索引参数
* 原子化属性更新器
 * AtomicIntegerFieldUpdater AtomicLongFieldUpdater AtomicReferenceFieldUpdater
 * 方打都是用反射机制实现 , 提供了静态创建方法 newUpdater
 * 对象属性必须是 volatile 类型 , 保证可见性
* 原子化累加器
 * DoubleAccumulator DoubleAdder LongAccumulator LongAddr
 * 不支持compareAndSet 仅仅用作累加 性能更好
 
## 
 



















