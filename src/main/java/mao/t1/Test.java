package mao.t1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Project name(项目名称)：java并发编程_自定义线程池
 * Package(包名): mao.t1
 * Class(类名): Test
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 14:38
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Test
{
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args)
    {
        ThreadPool threadPool = new ThreadPool(3, 3, TimeUnit.SECONDS, 10, new RejectPolicy<Runnable>()
        {
            @Override
            public void reject(BlockingQueue<Runnable> queue, Runnable task)
            {
                queue.put(task);

                /*boolean b = queue.offer(task, 1, TimeUnit.SECONDS);
                if (!b)
                {
                    log.warn("任务" + task + "添加失败");
                }*/

                //log.warn("丢弃任务：" + task);

                //throw new RuntimeException("线程池任务队列已满！task:" + task + "已被丢弃");
            }
        });

        for (int i = 0; i < 20; i++)
        {
            int finalI = i;
            try
            {
                threadPool.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        log.debug("" + finalI);
                    }
                });
            }
            catch (RuntimeException e)
            {
                //处理异常
                e.printStackTrace();
            }
        }
    }
}
