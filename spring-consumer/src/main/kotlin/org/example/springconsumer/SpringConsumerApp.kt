package org.example.springconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SpringConsumerApp

fun main(args: Array<String>) {
    runApplication<SpringConsumerApp>(*args)
}
