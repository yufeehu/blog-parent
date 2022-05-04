package com.hyh.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author huyuhui
 * 线程池的配置
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Value("${thread.pool.corePoolSize}")
    private int corePoolSize;
    @Value("${thread.pool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${thread.pool.queueCapacity}")
    private int queueCapacity;
    @Value("${thread.pool.keepAliveSeconds}")
    private int keepAliveSeconds;

    @Bean("taskExecutor")
    public Executor asyncServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix("thread");
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //执行初始化
        executor.initialize();
        log.info("core pool size：[ {} ]",this.corePoolSize);
        log.info("max pool size：[ {} ]",this.maxPoolSize);
        log.info("keep alive seconds：[ {} ]",this.corePoolSize);
        log.info("queue capacity：[ {} ]",this.queueCapacity);
        return executor;
    }

}
