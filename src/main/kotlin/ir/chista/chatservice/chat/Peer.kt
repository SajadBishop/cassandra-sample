package ir.chista.chatservice.chat

import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.UserDefinedType

@UserDefinedType("peer")
data class Peer(
    @CassandraType(type = CassandraType.Name.TEXT)
    @field:Column("peer_type")
    val peerType: String,

    @CassandraType(type = CassandraType.Name.TEXT)
    @field:Column("peer_id")
    val peerId: String
) {
    companion object {
        fun user(id: String): Peer = Peer("user", id)
        fun chat(id: String): Peer = Peer("chat", id)
    }
}
