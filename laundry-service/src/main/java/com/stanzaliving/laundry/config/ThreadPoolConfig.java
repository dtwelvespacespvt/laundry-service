package com.stanzaliving.laundry.config;

import com.stanzaliving.laundry.constant.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ThreadPoolConfig {

    @Value("${scheduled.thread.pool.size:2}")
    private int scheduledThreadPoolSize;

    @Bean(name = Constants.Bean.SCHEDULED_THREAD_POOL)
    public ScheduledExecutorService threadPoolTaskExecutor() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize);
    }
}
