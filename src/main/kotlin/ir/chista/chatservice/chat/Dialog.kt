package ir.chista.chatservice.chat

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.*

@Table("dialogs")
data class Dialog(

    @PrimaryKeyColumn(
        name = "owner",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    val owner: String,

    @PrimaryKeyColumn(name = "peer", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "peer")
    val peer: Peer,
)