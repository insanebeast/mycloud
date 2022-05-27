package com.pueeo.common.support.threadpool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池监控
 */
public interface ThreadPoolMonitor {
    ThreadPoolTaskExecutor getExecutor();
}
