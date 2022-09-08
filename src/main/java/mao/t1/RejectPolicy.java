package mao.t1;

import java.util.concurrent.BlockingQueue;

/**
 * Project name(项目名称)：java并发编程_自定义线程池
 * Package(包名): mao.t1
 * Interface(接口名): RejectPolicy
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 14:38
 * Version(版本): 1.0
 * Description(描述)： 自定义拒绝策略接口
 */

@FunctionalInterface
interface RejectPolicy<T>
{
    void reject(BlockingQueue<T> queue, T task);
}

