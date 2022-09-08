package mao.t1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Project name(项目名称)：java并发编程_自定义线程池
 * Package(包名): mao.t1
 * Class(类名): BlockingQueue
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 14:40
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class BlockingQueue<T>
{
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(BlockingQueue.class);

    /**
     * 任务队列
     */
    private final Deque<T> queue = new ArrayDeque<>();

    /**
     * 锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 生产者条件变量
     */
    private final Condition fullWaitSet = lock.newCondition();

    /**
     * 消费者条件变量
     */
    private final Condition emptyWaitSet = lock.newCondition();

    /**
     * 队列的容量
     */
    private int capacity;

    /**
     * 阻塞队列
     *
     * @param capacity 队列的容量
     */
    public BlockingQueue(int capacity)
    {
        this.capacity = capacity;
    }

    /**
     * 阻塞获取
     *
     * @return {@link T}
     */
    public T take()
    {
        lock.lock();
        try
        {
            //队列为空，则一直等待
            while (queue.isEmpty())
            {
                try
                {
                    //消费者条件变量
                    emptyWaitSet.await();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            //不为空
            T t = queue.removeFirst();
            //唤醒生产者条件变量里的线程
            fullWaitSet.signal();
            return t;
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 阻塞添加
     *
     * @param task 任务
     */
    public void put(T task)
    {
        lock.lock();
        try
        {
            //队列已满，则一直等待
            while (queue.size() == capacity)
            {
                log.debug("等待加入任务队列：" + task);
                try
                {
                    fullWaitSet.await();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            //队列不为满
            log.debug("加入任务队列：" + task);
            queue.addLast(task);
            emptyWaitSet.signal();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 带超时阻塞获取
     *
     * @param timeout 超时
     * @param unit    单位
     * @return {@link T}
     */
    public T poll(long timeout, TimeUnit unit)
    {
        lock.lock();
        try
        {
            long nanos = unit.toNanos(timeout);
            //队列为空，则一直等待
            while (queue.isEmpty())
            {
                try
                {
                    if (nanos <= 0)
                    {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            //不为空
            T t = queue.removeFirst();
            //唤醒生产者条件变量里的线程
            fullWaitSet.signal();
            return t;
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 带超时时间阻塞添加
     *
     * @param task     任务
     * @param timeout  超时
     * @param timeUnit 时间单位
     * @return boolean
     */
    public boolean offer(T task, long timeout, TimeUnit timeUnit)
    {
        lock.lock();
        try
        {
            long nanos = timeUnit.toNanos(timeout);
            //队列已满，则一直等待
            while (queue.size() == capacity)
            {
                if (nanos <= 0)
                {
                    return false;
                }
                log.debug("等待加入任务队列：" + task);
                try
                {
                    nanos = fullWaitSet.awaitNanos(nanos);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            //队列不为满
            log.debug("加入任务队列：" + task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 获取队列大小
     *
     * @return int
     */
    public int size()
    {
        lock.lock();
        try
        {
            return queue.size();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * @param rejectPolicy 拒绝策略
     * @param task         任务
     */
    public void tryPut(RejectPolicy<T> rejectPolicy, T task)
    {
        lock.lock();
        try
        {
            // 判断队列是否满
            if (queue.size() == capacity)
            {
                rejectPolicy.reject(this, task);
            }
            else
            {
                log.debug("加入任务队列：" + task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }
        finally
        {
            lock.unlock();
        }
    }
}
