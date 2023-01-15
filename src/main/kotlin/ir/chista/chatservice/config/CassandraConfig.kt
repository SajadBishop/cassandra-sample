package ir.chista.chatservice.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption
import com.datastax.oss.driver.api.core.config.DriverConfigLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.config.SessionBuilderConfigurer
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver
import java.time.Duration


@Configuration
class CassandraConfig : AbstractCassandraConfiguration() {

    override fun getKeyspaceName(): String {
        return "test_chat"
    }

    override fun getEntityBasePackages(): Array<String> {
        return arrayOf("ir.chista")
    }

    override fun getSchemaAction(): SchemaAction {
        return SchemaAction.RECREATE
    }

    override fun getSessionBuilderConfigurer(): SessionBuilderConfigurer? {
        return SessionBuilderConfigurer { cqlSessionBuilder ->
            cqlSessionBuilder.withConfigLoader(DriverConfigLoader.programmaticBuilder()
                .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofMillis(60000))
                .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(60000))
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000))
                .build()
            )
        }
    }
}