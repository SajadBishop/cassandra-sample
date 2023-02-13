package ir.chista.chatservice.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.platform.commons.util.AnnotationUtils
import org.springframework.util.StringUtils
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.shaded.org.awaitility.Awaitility
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.Callable

/**
 * JUnit 5 [BeforeAllCallback] extension to ensure a running Cassandra server.
 *
 * @author Mark Paluch
 * @see CassandraKeyspace
 */
internal class CassandraExtension() : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        val store = context.getStore(NAMESPACE)
        val cassandra = findAnnotation(context)
        val keyspace = store.getOrComputeIfAbsent(
            CassandraServer::class.java, {
            val container: CassandraContainer<*>? = runTestcontainer()
                System.setProperty("spring.cassandra.port", "" + container!!.getMappedPort(9042))
                System.setProperty("spring.cassandra.contact-points", "" + container.host)
                CassandraServer(
                    container.host, container.getMappedPort(9042),
                    CassandraServer.RuntimeMode.EMBEDDED_IF_NOT_RUNNING
                )
            }, CassandraServer::class.java
        )
        keyspace.before()
        val sessionFactory = Callable {
            CqlSession.builder()
                .addContactPoint(InetSocketAddress(keyspace.host(), keyspace.port()))
                .withLocalDatacenter("datacenter1")
                .build()
        }
        Awaitility.await().ignoreExceptions().untilAsserted {
            sessionFactory.call().close()
        }
        val session = store.getOrComputeIfAbsent<Class<CqlSession>, CqlSession>(
            CqlSession::class.java,
            {
                try {
                    return@getOrComputeIfAbsent sessionFactory.call()
                } catch (e: Exception) {
                    throw IllegalStateException(e)
                }
            },
            CqlSession::class.java
        )
        session.execute(
            String.format(
                "CREATE KEYSPACE IF NOT EXISTS %s \n"
                        + "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };",
                cassandra.keyspace
            )
        )
    }

    private fun runTestcontainer(): CassandraContainer<*>? {
        if (container != null) {
            return container
        }
        container = CassandraContainer(cassandraDockerImageName)
        container!!.withReuse(true)
        container!!.start()
        return container
    }

    private val cassandraDockerImageName: String
        get() = java.lang.String.format(
            "cassandra:%s",
            Optional.ofNullable(System.getenv("CASSANDRA_VERSION")).filter(StringUtils::hasText).orElse("3.11.10")
        )

    companion object {
        private val NAMESPACE = ExtensionContext.Namespace.create(CassandraExtension::class.java)
        private var container: CassandraContainer<*>? = null
        private fun findAnnotation(context: ExtensionContext): CassandraKeyspace {
            val testClass = context.requiredTestClass
            val annotation = AnnotationUtils.findAnnotation(testClass, CassandraKeyspace::class.java)
            return annotation.orElseThrow { IllegalStateException("Test class not annotated with @Cassandra") }
        }
    }
}