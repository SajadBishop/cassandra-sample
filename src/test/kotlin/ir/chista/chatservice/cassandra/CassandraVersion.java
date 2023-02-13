package ir.chista.chatservice.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.Version;
import org.springframework.util.Assert;

public final class CassandraVersion {

	/**
	 * Retrieve the Cassandra release version.
	 *
	 * @param session must not be {@literal null}.
	 * @return the release {@link Version}.
	 */
	public static Version getReleaseVersion(CqlSession session) {

		Assert.notNull(session, "Session must not be null");

		var resultSet = session.execute("SELECT release_version FROM system.local;");
		var row = resultSet.one();

		return Version.parse(row.getString(0));
	}
}