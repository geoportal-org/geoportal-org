package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * The type Batch configuration.
 */
@Log4j2
@Configuration
class BatchConfiguration {

    @Value("${batch.thread-pool.size}")
    private int batchThreadPoolSize;

    /**
     * Batch task executor task executor.
     *
     * @return the task executor
     */
    @Bean("batchTaskExecutor")
    TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(batchThreadPoolSize);
        executor.setMaxPoolSize(batchThreadPoolSize);
        executor.setThreadNamePrefix("batch_thread");
        executor.setDaemon(true);
        executor.initialize();

        return executor;
    }

}
