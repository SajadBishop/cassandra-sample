package ir.chista.chatservice;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption
import com.datastax.oss.driver.api.core.config.DriverConfigLoader
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.config.SessionBuilderConfigurer
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication
import java.time.Duration


@TestConfiguration
class TestCassandraConfig(
    @Value(value = "\${spring.cassandra.contact-points}") val cp: String) : AbstractCassandraConfiguration() {

    override fun getKeyspaceName(): String {
        return "testSpace"
    }

    override fun getEntityBasePackages(): Array<String> {
        return arrayOf("ir.chista")
    }

    override fun getSchemaAction(): SchemaAction {
        return SchemaAction.CREATE
    }

    override fun getContactPoints(): String {
        return cp
    }

    override fun getKeyspaceCreations(): List<CreateKeyspaceSpecification> {
        return listOf(CreateKeyspaceSpecification
                .createKeyspace(keyspaceName)
                .ifNotExists(true)
                .withNetworkReplication(DataCenterReplication.of(localDataCenter!!, 1))
        )
    }

    override fun getSessionBuilderConfigurer(): SessionBuilderConfigurer? {
        return SessionBuilderConfigurer { cqlSessionBuilder -> cqlSessionBuilder.withConfigLoader(
                DriverConfigLoader.programmaticBuilder()
                    .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofMillis(60000))
                    .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(60000))
                    .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000))
                    .withDuration(DefaultDriverOption.METADATA_SCHEMA_WINDOW, Duration.ZERO)
                    .build()
            )
        }
    }
}