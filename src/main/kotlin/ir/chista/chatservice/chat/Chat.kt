package ir.chista.chatservice.chat

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("chats")
data class Chat(

    @PrimaryKeyColumn(
        name = "chat_id",
        ordinal = 1,
        type = PrimaryKeyType.PARTITIONED
    )
    var chatId: UUID,

    @Column
    var title: String,

    @Column
    var description: String?,

    @field:Column("created_by")
    var createdBy: String,

    @field:Column("created_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    var createdAt: Instant = Instant.now(),

    @field:Column
    @CassandraType(type = CassandraType.Name.BIGINT)
    var pts: Long = 0
)