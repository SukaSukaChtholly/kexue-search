package com.kexue.search.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
public class CustomThreadPool {

    @Autowired
    private TaskThreadPoolConfig config;

    @Bean("customAsyncThreadPool")
    public Executor customAsyncThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        //核心线程数
        executor.setCorePoolSize(config.getCorePoolSize());
        //任务队列的大小
        executor.setQueueCapacity(config.getQueueCapacity());
        //线程池名的前缀
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        //允许线程的空闲时间30秒
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(config.getAwaitTerminationSeconds());

        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        /**
         * 特殊说明：
         * 1. 这里演示环境，拒绝策略咱们采用抛出异常
         * 2.真实业务场景会把缓存队列的大小会设置大一些，
         * 如果，提交的任务数量超过最大线程数量或将任务环缓存到本地、redis、mysql中,保证消息不丢失
         * 3.如果项目比较大的话，异步通知种类很多的话，建议采用MQ做异步通知方案
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}