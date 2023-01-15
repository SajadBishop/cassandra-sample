package ir.chista.chatservice.user

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("user_blocks")
data class UserBlock(

    @PrimaryKeyColumn(
        name = "blocker_user",
        type = PrimaryKeyType.PARTITIONED)
    val blockerUser: String,

    @PrimaryKeyColumn(
        name = "blocked_user",
        type = PrimaryKeyType.CLUSTERED
    )
    val blockedUser: String,

    @field:Column("blocked_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    val blockedAt: Instant?
)