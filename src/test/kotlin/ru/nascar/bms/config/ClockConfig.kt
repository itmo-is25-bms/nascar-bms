package ru.nascar.bms.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@Configuration
class ClockConfig {
    @Bean
    @Primary
    fun clock(): Clock {
        return Clock.fixed(Instant.parse("2024-12-12T12:12:12.123Z"), ZoneOffset.UTC)
    }
}