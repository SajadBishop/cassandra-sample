package ir.chista.chatservice

import com.datastax.oss.driver.api.core.uuid.Uuids
import ir.chista.chatservice.chat.Message
import ir.chista.chatservice.chat.MessageRepository
import ir.chista.chatservice.chat.Peer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit


@Testcontainers
@DataCassandraTest
@Import(TestCassandraConfig::class)
internal class SimpleCassandraTest {

    companion object {
        @Container
        @JvmStatic
        val cassandra = CassandraContainer("cassandra:4.0.7")
            //.withInitScript("init.cql")
            .withReuse(true)
            .waitingFor(Wait.forLogMessage(".*Startup complete.*\\n", 1))
            .withExposedPorts(9042) as CassandraContainer<*>


        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.cassandra.contact-points")
            { "${cassandra.host}:${cassandra.firstMappedPort}" }
        }
    }

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Test
    fun `Given valid message record, record should be safely saved`() {
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

        val approxComparison =
            { a: Instant, b: Instant -> a.truncatedTo(ChronoUnit.MILLIS).compareTo(b.truncatedTo(ChronoUnit.MILLIS)) }
        assertThat(messages[0])
            .usingComparatorForType(approxComparison, Instant::class.java)
            .usingRecursiveComparison()
            .isEqualTo(message)
    }
}