package ru.nascar.bms.config

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class EmbeddedPostgresConfig {
    @Bean
    fun embeddedPostgres(): EmbeddedPostgres {
        return EmbeddedPostgres.builder()
            .setServerConfig("unix_socket_directories", "")
            .start()
    }

    @Bean
    @Primary
    fun dataSource(embeddedPostgres: EmbeddedPostgres): DataSource {
        return embeddedPostgres.postgresDatabase
    }
}