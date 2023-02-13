package ir.chista.chatservice.cassandra;

import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.Assumptions;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Utility for Cassandra server use. This utility can start a Cassandra instance, reuse a running instance or simply
 * require a running Cassandra server (will skip the test if Cassandra is not running).
 *
 * @author Mark Paluch
 */
record CassandraServer(String host, int port, CassandraServer.RuntimeMode runtimeMode) {

	/**
	 * Require a running instance on {@code host:port}. Fails with {@link AssumptionViolatedException} if Cassandra is not
	 * running.
	 *
	 * @param host must not be {@literal null} or empty.
	 * @param port must be between 0 and 65535.
	 * @return the {@link CassandraServer} rule
	 */
	public static CassandraServer requireRunningInstance(String host, int port) {
		return new CassandraServer(host, port, RuntimeMode.REQUIRE_RUNNING_INSTANCE);
	}

	/**
	 * Start an embedded Cassandra instance on {@code host:port} if Cassandra is not running already.
	 *
	 * @param host must not be {@literal null} or empty.
	 * @param port must be between 0 and 65535.
	 * @return the {@link CassandraServer} rule
	 */
	public static CassandraServer embeddedIfNotRunning(String host, int port) {
		return new CassandraServer(host, port, RuntimeMode.EMBEDDED_IF_NOT_RUNNING);
	}

	/**
	 * @param host must not be {@literal null} or empty.
	 * @return {@literal true} if the TCP port accepts a connection.
	 */
	public static boolean isConnectable(String host, int port) {

		Assert.hasText(host, "Host must not be null or empty!");

		try (var socket = new Socket()) {

			socket.setSoLinger(true, 0);
			socket.connect(new InetSocketAddress(host, port), (int) TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	protected void before() {

		if (runtimeMode == RuntimeMode.REQUIRE_RUNNING_INSTANCE) {
			Assumptions.assumeTrue(isConnectable(getHost(), getPort()),
					() -> String.format("Cassandra is not reachable at %s:%s.", getHost(), getPort()));
		}

		if (runtimeMode == RuntimeMode.EMBEDDED_IF_NOT_RUNNING) {
			if (isConnectable(getHost(), getPort())) {
				return;
			}
		}
	}

	enum RuntimeMode {
		REQUIRE_RUNNING_INSTANCE, EMBEDDED_IF_NOT_RUNNING;
	}
}