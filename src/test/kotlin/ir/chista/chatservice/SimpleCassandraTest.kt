package ir.chista.chatservice

import com.datastax.oss.driver.api.core.uuid.Uuids
import ir.chista.chatservice.chat.Message
import ir.chista.chatservice.chat.MessageRepository
import ir.chista.chatservice.chat.Peer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit


@Testcontainers
@DataCassandraTest
internal class SimpleCassandraTest {

    companion object {
        @Container
        @JvmStatic
        val cassandra = CassandraContainer("cassandra:4.0.7")
            .withInitScript("init.cql")
            .withReuse(true)
            .withExposedPorts(9042) as CassandraContainer<*>

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.cassandra.contact-points") { "${cassandra.host}:${cassandra.firstMappedPort}" }
        }
    }

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Test
    fun givenValidMessageRecord_whenSavingIt_thenRecordIsSaved() {
        val privateRoom = "userA-userB"
        val message = Message(
            Peer.user(privateRoom),
            Uuids.timeBased(),
            "I'm back.",
            "sajad",
            LocalDateTime.now().toInstant(ZoneOffset.UTC)
        )
        messageRepository.save(message)
        val messages = messageRepository.findAllByPeer(Peer.user(privateRoom))
        assertThat(messages[0]).usingComparatorForType(
            { a, b -> a.truncatedTo(ChronoUnit.MILLIS).compareTo(b.truncatedTo(ChronoUnit.MILLIS)) },
            Instant::class.java
        ).usingRecursiveComparison().isEqualTo(message)
    }
}