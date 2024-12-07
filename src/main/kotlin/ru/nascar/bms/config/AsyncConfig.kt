package ru.nascar.bms.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class AsyncConfig {

    companion object {
        const val EVENT_TASK_EXECUTOR = "eventTaskExecutor"
    }

    @Bean
    @Qualifier(EVENT_TASK_EXECUTOR)
    fun eventTaskExecutor(): TaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 4

            setThreadNamePrefix("event-")
            setWaitForTasksToCompleteOnShutdown(true)

            initialize()
        }
    }
}