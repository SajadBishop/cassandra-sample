package ir.chista.chatservice.user

import User
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : CassandraRepository<User, UUID> {
}