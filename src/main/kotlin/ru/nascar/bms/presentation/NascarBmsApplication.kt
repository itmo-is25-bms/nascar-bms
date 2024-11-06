package ru.nascar.bms.presentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ru.nascar"])
@ConfigurationPropertiesScan
class NascarBmsApplication

fun main(args: Array<String>) {
	runApplication<NascarBmsApplication>(*args)
}
