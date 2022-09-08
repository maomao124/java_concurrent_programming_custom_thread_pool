package mao.t1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ThreadPool.class);

    /**
     * 任务队列
     */
    private final BlockingQueue<Runnable> taskQueue;

    /**
     * 线程集合
     */
    private final HashSet<Worker> workers = new HashSet<>();

    /**
     * 核心线程数大小
     */
    private final int coreSize;

    /**
     * 获取任务时的超时时间
     */
    private final long timeout;

    /**
     * 时间单位
     */
    private final TimeUnit timeUnit;

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


    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task)
    {

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
            // 当 task 不为空，执行任务
            //当 task 执行完毕，再接着从任务队列获取任务并执行
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null)
            {
                try
                {
                    log.debug("正在执行任务：" + task);
                    task.run();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    //设置为空，不然会影响下一次的判断
                    task = null;
                }
            }
            //队列里也没有任务了
            //移除
            synchronized (workers)
            {
                log.debug("worker 被移除" + this);
                workers.remove(this);
            }
        }
    }

}
