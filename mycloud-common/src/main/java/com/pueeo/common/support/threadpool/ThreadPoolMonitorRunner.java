package com.pueeo.common.support.threadpool;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池监控
 */
@Component
@Slf4j
public class ThreadPoolMonitorRunner implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final List<ThreadPoolTaskExecutor> executors = Lists.newArrayList();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);


    @Override
    public void run(ApplicationArguments args) {
        Map<String, ThreadPoolMonitor> monitorMap = applicationContext.getBeansOfType(ThreadPoolMonitor.class);
        monitorMap.values().forEach(monitor-> {
            executors.add(monitor.getExecutor());
        });
        scheduler.scheduleAtFixedRate(this::monitor, 0, 1, TimeUnit.MINUTES);

        //线程池优雅关闭
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> executors.forEach(executor -> {
//            if (!ObjectUtils.isEmpty(executor)){
//                MoreExecutors.shutdownAndAwaitTermination(scheduler, 10L, TimeUnit.SECONDS);
//            }
//        })));

    }

    private void monitor() {
        executors.forEach(executor-> {
            ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();
            String threadNamePrefix = executor.getThreadNamePrefix();
            long taskCount = threadPool.getTaskCount();
            long completedTaskCount = threadPool.getCompletedTaskCount();
            long queueSize = threadPool.getQueue().size();
            long largestPoolSize = threadPool.getLargestPoolSize();
            long poolSize = executor.getPoolSize();
            long activeCount = executor.getActiveCount();
            log.info("ThreadPool Monitor: namePrefix={}, taskCount={}, queueCount={} completedTaskCount={}, largestPoolSize={}, poolSize={}, activeCount={}",
                    threadNamePrefix, taskCount, queueSize, completedTaskCount, largestPoolSize, poolSize, activeCount);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
