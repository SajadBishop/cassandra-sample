package ir.chista.chatservice

import com.datastax.oss.driver.api.core.uuid.Uuids
import com.github.javafaker.Faker
import ir.chista.chatservice.chat.Message
import ir.chista.chatservice.chat.MessageRepository
import ir.chista.chatservice.chat.Peer
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.cassandra.core.CassandraAdminTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream

private val log = KotlinLogging.logger {}

@Component
class PlayTest(val messageRepository: MessageRepository,
               val kafkaTemplate: KafkaTemplate<String, Any>,
               val adminTemplate: CassandraAdminTemplate)
    : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        /*
        adminTemplate.createTable(
            true,
            CqlIdentifier.fromCql("book"),
            Book::class.java,
            HashMap<String, Any>()
        )
        */

        // testKafka(message)
    }

    private fun testKafka(message: Any) {
        val future: CompletableFuture<SendResult<String, Any>> =
            kafkaTemplate.send("testChatTopic", "Hi!")
        future.whenComplete { t, u ->
            if (u != null) {
                println("Unable to send message due to: " + u.message)
            } else {
                println("Sent message with offset=[" + t.recordMetadata.offset() + "]")
            }
        }

        kafkaTemplate.send("testChatTopic", message)
        val faker = Faker.instance()
        val interval = Flux.interval(Duration.ofMillis(1000))
        val quotes: Flux<String> = Flux.fromStream(Stream.generate { faker.hobbit().quote() })
        Flux.zip(interval, quotes)
            .map<Any> { it: Tuple2<Long, String> ->
                kafkaTemplate.send(
                    "hobbit",
                    faker.random().nextInt(42).toString(),
                    it.t2
                )
            }.blockLast()
    }
}