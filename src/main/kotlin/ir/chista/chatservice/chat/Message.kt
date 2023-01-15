package ir.chista.chatservice.chat

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("messages")
data class Message(

    @PrimaryKeyColumn(name = "peer", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "peer")
    val peer: Peer,

    @PrimaryKeyColumn(
        name = "id",
        ordinal = 3,
        type = PrimaryKeyType.CLUSTERED,
        ordering = Ordering.DESCENDING)
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    val messageId: UUID,

    @field:Column("content")
    val content: String,

    @field:Column("from_id")
    val fromId: String,

    @field:Column("sent_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    val sentAt: Instant,

    @field:Column("edited_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    val editedAt: Instant? = null
)
