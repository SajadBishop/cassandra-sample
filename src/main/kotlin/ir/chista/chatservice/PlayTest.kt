package ir.chista.chatservice


import com.datastax.oss.driver.api.core.uuid.Uuids
import ir.chista.chatservice.chat.Message
import ir.chista.chatservice.chat.MessageRepository
import ir.chista.chatservice.chat.Peer
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.cassandra.core.CassandraOperations
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset

private val log = KotlinLogging.logger {}


/*
        TODO:
               - Add sample batch operation
               - Use primary key class to define a primary key
               - Define a simple service with basic models
 */
@Component
class PlayTest(val messageRepository: MessageRepository,
                val cassandraOps: CassandraOperations
) : ApplicationRunner {

        override fun run(args: ApplicationArguments) {
                val message = Message(
                        Peer.user("min"),
                        Uuids.timeBased(),
                        "I'm back.",
                        "sajad",
                        LocalDateTime.now().toInstant(ZoneOffset.UTC)
                )
                messageRepository.save(message)
                cassandraOps.batchOps()
                        .insert(message.copy(messageId = Uuids.timeBased(), content="This is"),
                                message.copy(messageId = Uuids.timeBased(), content="important."))
                        .execute()

                val messages = messageRepository.findAllByPeer(Peer.user("min"))
                messages.sortedBy { it.messageId }.forEach { println(it) }
        }
}
