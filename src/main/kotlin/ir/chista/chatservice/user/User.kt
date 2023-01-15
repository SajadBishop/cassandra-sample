import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.*

@Table("users")
data class User(
    @PrimaryKeyColumn(
        name = COLUMN_USERID,
        type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    val userId: UUID,

    @field:Column(COLUMN_FIRSTNAME)
    val firstName: String,

    @field:Column(COLUMN_LASTNAME)
    val lastName: String,

    @field:Column(COLUMN_CREATED)
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    val createdAt: Date,
) {
    companion object {
        /** Column names in the DB. */
        const val COLUMN_USERID: String = "user_id"
        const val COLUMN_FIRSTNAME: String = "first_name"
        const val COLUMN_LASTNAME: String = "last_name"
        const val COLUMN_CREATED: String = "created_at"
    }
}

