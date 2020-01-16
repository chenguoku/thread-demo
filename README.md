# 多线程的使用

## 线程3种创建方式

1. 实现Runnable接口
2. 继承Thread类
3. 实现Callable接口

## 创建线程池

### 常用线程池：

1.  `Executors.newFixedThreadPool(3);`定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。 
2.  `Executors.newSingleThreadExecutor();`单线程 的线程池 
3. `Executors.newCachedThreadPool();` 可缓存线程池，若线程池长度超过处理需要，则回收空线程，否则创建新线程，线程规模可无限大(`Integer.MAX_VALUE`)。 
4. `Executors.newScheduledThreadPool(5);` 定长线程池，支持定时及周期性任务执行，类似Timer。 



### `ThreadPoolExecutor`参数说明

* `corePoolSize` ： 核心线程数量，当有新任务在execute()方法提交时，会执行以下判断： 

		>  1.  如果运行的线程少于 `corePoolSize`，则创建新线程来处理任务，即使线程池中的其他线程是空闲的； 
		>  2.  如果线程池中的线程数量大于等于 `corePoolSize` 且小于 `maximumPoolSize`，则只有当`workQueue`满时才创建新的线程去处理任务； 
		>  3.  如果设置的`corePoolSize` 和 `maximumPoolSize`相同，则创建的线程池的大小是固定的，这时如果有新任务提交，若`workQueue`未满，则将请求放入`workQueue`中，等待有空闲的线程去从`workQueue`中取任务并处理； 
		>  4.  如果运行的线程数量大于等于`maximumPoolSize`，这时如果`workQueue`已经满了，则通过handler所指定的策略来处理任务； 

* `maximumPoolSize` ：线程池所能容纳的最大线程数。

* `keepAliveTime` ：非核心线程闲置时的超时时长。超过该时长，非核心线程就会被回收。

* `unit` ：`keepAliveTime`的时间单位。

* `workQueue` ： 等待队列，当任务提交时，如果线程池中的线程数量大于等于`corePoolSize`的时候，把该任务封装成一个Worker对象放入等待队列； 

>  *  SynchronousQueue 是无界的，是一种无缓冲的等待队列 
>  *  LinkedBlockingQueue  是无界的，是一个无界缓存的等待队列。 
>  *  ArrayBlockingQueue  是有界的，是一个有界缓存的等待队列。 

* `threadFactory`： 它是`ThreadFactory`类型的变量，用来创建新线程。

> 默认使用`Executors.defaultThreadFactory()` 来创建线程。使用默认的`ThreadFactory`来创建线程时，会使新创建的线程具有相同的NORM_PRIORITY优先级并且是非守护线程，同时也设置了线程的名称。 

* `handler` ： 它是`RejectedExecutionHandler`类型的变量，表示线程池的饱和策略。如果阻塞队列满了并且没有空闲的线程，这时如果继续提交任务，就需要采取一种策略处理该任务。线程池提供了4种策略： 

> 1.  `AbortPolicy`：直接抛出异常，这是默认策略； 
> 2.  `CallerRunsPolicy`：用调用者所在的线程来执行任务； 
> 3.  `DiscardOldestPolicy`：丢弃阻塞队列中靠最前的任务，并执行当前任务； 
> 4. ` DiscardPolicy`：直接丢弃任务； 



## `SpringBoot`中配置线程池

### 第一步 配置线程池

#### 创建线程池的Bean

```java
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

    @Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${async.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
```



#### 编写配置文件，设置线程数、队列大小等

```java
# 异步线程配置
async:
  executor:
    thread:
      # 配置核心线程数
      core_pool_size: 5
      # 配置最大线程数
      max_pool_size: 5
      # 配置队列大小
      queue_capacity: 99999
      name:
        # 配置线程池中的线程的名称前缀
        prefix: async-service-
```



### 第二步 线程池的使用

#### 定义一个函数式接口

```java
@FunctionalInterface
public interface ExecutorFunctional {

    void execute() throws Exception;

}
```



#### 定义一个异步调用的接口

```java
public interface AsyncService {

    void executeAsync(ExecutorFunctional target);

}
```



#### 实现异步调用接口

```java
@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(ExecutorFunctional target) {
        try {
            target.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```



### 异步的调用

```java
@SpringBootTest
@Slf4j
public class ThreadPoolTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            asyncService.executeAsync(() -> {
                int random = (int) (2 + Math.random() * 8);
                log.info(Thread.currentThread().getName() + "睡{}秒", random);
                Thread.sleep(random * 1000);
                latch.countDown();
                log.info(Thread.currentThread().getName() + "睡{}秒,执行完毕", random);
            });
        }

        log.info("主线程执行完毕");

        latch.await();

    }

}
```



## `ForkJoin`的使用

>  `ForkJoin`是`Java7`提供的原生多线程并行处理框架，其基本思想是将大任务分割成小任务，最后将小任务聚合起来得到结果。fork是分解的意思, join是收集的意思   
>
>  在fork/join框架中，若某个子问题由于等待另一个子问题的完成而无法继续执行。那么处理该子问题的线程会主动寻找其他尚未运行完成的子问题来执行。这种方式减少了线程的等待时间，提高了性能。 



### `RecursiveAction`，用于没有返回结果的任务

```java
@Component
@Slf4j
public class ActionForkJoin extends RecursiveAction {

    /**
     * 阀值,每组的大小
     */
    private static final Integer THRES_HOLD = 30;

    //要处理的数据
    private List records;

    public ActionForkJoin() {
    }

    public ActionForkJoin(List records) {
        this.records = records;
    }

    /**
     * 计算分组
     *
     * @return
     */
    @Override
    protected void compute() {
        //每组30个
        if (records.size() <= THRES_HOLD) {
            computeDirectly();
            return;
        }
        //如果要处理的数据大于等于30，则进行分组转换
        int size = records.size();
        //第一个分组
        ActionForkJoin aTask = new ActionForkJoin(records.subList(0, size / 2));
        //第二个分组
        ActionForkJoin bTask = new ActionForkJoin(records.subList(size / 2, records.size()));
        //两个任务并发执行起来
        invokeAll(aTask, bTask);

    }
    
    /**
     * 真正处理数据的逻辑
     */
    private void computeDirectly() {
        //针对每组 处理的业务逻辑
        //这里的records的值，就是每组的值
        System.out.println(Thread.currentThread().getName() + " 开始 " + records.size());
        System.out.println(Thread.currentThread().getName() + " " + records.toString());
        System.out.println(Thread.currentThread().getName() + "结束");
    }
}
```



```java
public class ActionMain {

    public static void main(String[] args) throws InterruptedException {

        List<String> dataList = getDataList();

        // 线程池中的线程数
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        // dataList:需要处理的数据
        ActionForkJoin actionForkJoin = new ActionForkJoin(dataList);
        forkJoinPool.execute(actionForkJoin);

        //阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
//        forkJoinPool.awaitTermination(100, TimeUnit.SECONDS);
        forkJoinPool.awaitQuiescence(100, TimeUnit.SECONDS);
    }

    public static List<String> getDataList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }

        return list;
    }

}
```



### `RecursiveTask`，用于有返回值的任务



```java
@Component
@Slf4j
public class TaskForkJoin extends RecursiveTask<Long> {

    /**
     * 阀值
     */
    private static final Integer THRES_HOLD = 30;

    //要处理的数据
    private List records;

    public TaskForkJoin() {
    }

    public TaskForkJoin(List records) {
        this.records = records;
    }

    /**
     * 计算分组
     *
     * @return
     */
    @Override
    protected Long compute() {
        //每组30个
        if (records.size() <= THRES_HOLD) {
            return computeDirectly();
        }
        //如果要处理的数据大于等于30，则进行分组转换
        int size = records.size();
        //第一个分组
        TaskForkJoin aTask = new TaskForkJoin(records.subList(0, size / 2));
        //第二个分组
        TaskForkJoin bTask = new TaskForkJoin(records.subList(size / 2, records.size()));
        //两个任务并发执行起来
        invokeAll(aTask, bTask);

        return aTask.join() + bTask.join();
    }


    /**
     * 真正处理数据的逻辑
     */
    private Long computeDirectly() {
        //针对每组 处理的业务逻辑
        //这里的records的值，就是每组的值
        System.out.println(Thread.currentThread().getName() + " records 的数量" + records.size());
        Long num = 0L;
        for (Object obj : records) {
            long l = Long.parseLong(String.valueOf(obj));
            num += l;
        }
        return num;
    }
}
```



```java
public class TaskMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Long> list = getDataList();

        // 线程池中的线程数
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        // list:需要处理的数据
        TaskForkJoin taskForkJoin = new TaskForkJoin(list);
        ForkJoinTask<Long> submitResult = forkJoinPool.submit(taskForkJoin);

        System.out.println(submitResult.get());
    }

    public static List<Long> getDataList() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Long.parseLong(String.valueOf(i)));
        }

        return list;
    }

}
```

