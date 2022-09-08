package mao.t1;

import java.util.ArrayDeque;
import java.util.Deque;
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
     * 任务队列
     */
    private Deque<T> queue = new ArrayDeque<>();

    /**
     * 锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 生产者条件变量
     */
    private Condition fullWaitSet = lock.newCondition();

    /**
     * 消费者条件变量
     */
    private Condition emptyWaitSet = lock.newCondition();

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




}
