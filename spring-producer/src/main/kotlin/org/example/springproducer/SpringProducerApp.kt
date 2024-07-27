package org.example.springproducer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SpringProducerApp

fun main(args: Array<String>) {
    runApplication<SpringProducerApp>(*args)
}
