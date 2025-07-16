package com.stanzaliving.laundry.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Component
    public static class LaundryAsyncConfigurer implements AsyncConfigurer {

        @Override
        public Executor getAsyncExecutor() {
            return Executors.newCachedThreadPool();
        }

        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new LaundryAsyncUncaughtExceptionHandler();
        }
    }

    @Log4j2
    public static class LaundryAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("Async job failed", ex);
        }
    }

}
