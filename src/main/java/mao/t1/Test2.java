package mao.t1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Project name(项目名称)：java并发编程_自定义线程池
 * Package(包名): mao.t1
 * Class(类名): Test2
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 20:38
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Test2
{
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    /**
     * 得到int随机
     *
     * @param min 最小值
     * @param max 最大值
     * @return int
     */
    public static int getIntRandom(int min, int max)
    {
        if (min > max)
        {
            min = max;
        }
        return min + (int) (Math.random() * (max - min + 1));
    }

    public static void main(String[] args)
    {
        ThreadPool threadPool = new ThreadPool(8, 3, TimeUnit.SECONDS, 16, new RejectPolicy<Runnable>()
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

        for (int i = 0; i < 200; i++)
        {
            try
            {
                int finalI = i;
                threadPool.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        long result = 0;
                        for (int j = 0; j < 2000000000; j++)
                        {
                            //result = result + getIntRandom(0, 2);
                            result = (result + j) / 2 + j;
                        }
                        log.info("序号" + finalI + "的结果：" + result);
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
