package ir.chista.chatservice.chat

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("chat_members")
data class ChatMember(

    @PrimaryKeyColumn(
        name = "chat_id",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED
    )
    var chatId: UUID,

    @PrimaryKeyColumn(
        name = "user_id",
        ordinal = 1,
        type = PrimaryKeyType.CLUSTERED
    )
    var userId: String,

    @field:Column("joined_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    var joinedAt: Instant,
)