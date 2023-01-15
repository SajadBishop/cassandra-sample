package ir.chista.chatservice.chat

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : CassandraRepository<Message, Peer> {

    fun findAllByPeer(peer: Peer): List<Message>
}