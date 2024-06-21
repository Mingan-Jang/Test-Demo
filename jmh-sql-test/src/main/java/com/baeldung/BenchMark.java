package com.baeldung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class BenchMark {

	private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/jmh_sql_test";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456";

	private static final HikariDataSource dataSource;

	static {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(JDBC_URL);
		config.setUsername(USERNAME);
		config.setPassword(PASSWORD);
		config.setMaximumPoolSize(50);
		dataSource = new HikariDataSource(config);
	}

	@Fork(1)
	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Warmup(iterations = 2, time = 3)
	@Measurement(iterations = 5)
	public void testSQLQueryWithForceIndex() {
        try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			String sql = "SELECT COUNT(*) FROM ("
					+ "SELECT gupid, status, MAX(status) OVER (PARTITION BY gupid) AS max_status "
					+ "FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1) " + "WHERE gupid = 'group5' "
					+ "AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00' "
					+ "AND delete_flag = false) AS subquery " + "WHERE status = max_status;";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Do nothing, just iterate over the result set
			}
		} catch (SQLException e) {
			System.out.println("SQL query failed!");
			e.printStackTrace();
		}
	}

	@Fork(1)
	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Warmup(iterations = 2, time = 3)
	@Measurement(iterations = 5)
	public void testSQLQueryWithoutForceIndex() {
        try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			String sql = "SELECT COUNT(*) FROM ("
					+ "SELECT creator, status, MAX(status) OVER (PARTITION BY gupid) AS max_status "
					+ "FROM jmh_sql_test.sub_index_test sit " + "WHERE gupid = 'group5' "
					+ "AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00' "
					+ "AND delete_flag = false) AS subquery " + "WHERE status = max_status;";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Do nothing, just iterate over the result set
			}
		} catch (SQLException e) {
			System.out.println("SQL query failed!");
			e.printStackTrace();
		}
	}

}
