package ru.nascar.bms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
@ConfigurationPropertiesScan
class NascarBmsApplication

fun main(args: Array<String>) {
	runApplication<NascarBmsApplication>(*args)
}
