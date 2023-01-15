package ir.chista.chatservice.user

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface UserBlockRepository : CassandraRepository<UserBlock, String> {

    fun findAllByBlockerUser(userId: String): List<UserBlock>
}