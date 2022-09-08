package mao.t1;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Project name(项目名称)：java并发编程_自定义线程池
 * Package(包名): mao.t1
 * Class(类名): ThreadPool
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 15:03
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class ThreadPool
{
    /**
     * 任务队列
     */
    private BlockingQueue<Runnable> taskQueue;

    /**
     * 线程集合
     */
    private HashSet<Worker> workers = new HashSet<>();

    /**
     * 核心线程数大小
     */
    private int coreSize;

    /**
     * 获取任务时的超时时间
     */
    private long timeout;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 拒绝策略
     */
    private RejectPolicy<Runnable> rejectPolicy;

    /**
     * 构造方法，线程池
     *
     * @param coreSize     核心线程数大小
     * @param timeout      超时时间
     * @param timeUnit     时间单位
     * @param queueSize    队列大小
     * @param rejectPolicy 拒绝策略
     */
    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueSize,
                      RejectPolicy<Runnable> rejectPolicy)
    {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueSize);
        this.rejectPolicy = rejectPolicy;
    }


    class Worker extends Thread
    {
        /**
         * 任务
         */
        private Runnable task;

        /**
         * 构造方法
         *
         * @param task 任务
         */
        public Worker(Runnable task)
        {
            this.task = task;
        }


        @Override
        public void run()
        {

        }
    }

}
