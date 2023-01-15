package ir.chista.chatservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup


@SpringBootApplication()
class ChatServiceApplication

fun main(args: Array<String>) {
	val application = SpringApplication(ChatServiceApplication::class.java)
	application.applicationStartup = BufferingApplicationStartup(10000)
	application.run(*args)
}
