package io.grx.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 创建单例线程池
 */
public class ThreadPoolService {

    private static final int DEFAULT_CORE_SIZE=100;
    private static final int MAX_QUEUE_SIZE=500;
    private volatile static ThreadPoolExecutor executor;

    // 获取单例的线程池对象
    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (ThreadPoolService.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE,// 核心线程数
                            MAX_QUEUE_SIZE, // 最大线程数
                            Integer.MAX_VALUE, // 闲置线程存活时间
                            TimeUnit.MILLISECONDS,// 时间单位
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
                            Executors.defaultThreadFactory()// 线程工厂
                    );
                }
            }
        }
        return executor;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    // 从线程队列中移除对象
    public void cancel(Runnable runnable) {
        if (executor != null) {
            executor.getQueue().remove(runnable);
        }
    }
}
