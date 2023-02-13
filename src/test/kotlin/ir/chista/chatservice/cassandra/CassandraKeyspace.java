package ir.chista.chatservice.cassandra;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(CassandraExtension.class)
public @interface CassandraKeyspace {

	/**
	 * Name of the desired keyspace to be provided.
	 *
	 * @return
	 */
	String keyspace() default "example";
}