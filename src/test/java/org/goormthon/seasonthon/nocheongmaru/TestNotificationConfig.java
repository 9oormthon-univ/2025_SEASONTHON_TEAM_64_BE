package org.goormthon.seasonthon.nocheongmaru;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;

@TestConfiguration
public class TestNotificationConfig {
    
    @Bean(name = "taskExecutor")
    public SyncTaskExecutor taskExecutor() {
        return new SyncTaskExecutor();
    }
    
    @Bean
    @Primary
    public InMemoryPushSender inMemoryPushSender() {
        return new InMemoryPushSender();
    }
}
