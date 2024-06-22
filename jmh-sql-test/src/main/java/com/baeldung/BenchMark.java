package com.baeldung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Fork(1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 5)
public class BenchMark {

	@Benchmark
	public void testIndexlFieldsUsingWhere_G1(BenchmarkState state, Blackhole blackhole) {
		String sql = "SELECT COUNT(*) FROM (SELECT creator, status, MAX(status) OVER (PARTITION BY gupid) AS max_status "
				+ "FROM jmh_sql_test.sub_index_test sit " + "WHERE gupid = 'group5' "
				+ "AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00' "
				+ "AND delete_flag = false) AS subquery " + "WHERE status = max_status;";
		executeQuery(state, sql, blackhole);
	}

	@Benchmark
	public void testIndexlFieldsUsingIndex_G1(BenchmarkState state, Blackhole blackhole) {
		String sql = "SELECT COUNT(*) FROM (SELECT gupid, status, MAX(status) OVER (PARTITION BY gupid) AS max_status "
				+ "FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1) " + "WHERE gupid = 'group5' "
				+ "AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00' "
				+ "AND delete_flag = false) AS subquery " + "WHERE status = max_status;";
		executeQuery(state, sql, blackhole);
	}

	@Benchmark
	public void testIndexlFieldsUsingPartialIndex_G2(BenchmarkState state, Blackhole blackhole) {
		String sql = "SELECT COUNT(*) FROM (SELECT gupid, status, MAX(status) OVER (PARTITION BY gupid) AS max_status "
				+ "FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1) "
				+ "WHERE createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00') AS subquery "
				+ "WHERE status = max_status;";

		executeQuery(state, sql, blackhole);
	}

	@Benchmark
	public void testIndexlFieldsUsingWhere_G2(BenchmarkState state, Blackhole blackhole) {
		String sql = "SELECT COUNT(*) FROM (SELECT gupid, status, MAX(status) OVER (PARTITION BY gupid) AS max_status "
				+ "FROM jmh_sql_test.sub_index_test sit "
				+ "WHERE createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00') AS subquery "
				+ "WHERE status = max_status;";

		executeQuery(state, sql, blackhole);
	}

	@Benchmark
	public void testAllFieldsUsingIndex_G3(BenchmarkState state, Blackhole blackhole) {
		String sql = "SELECT COUNT(*) FROM (SELECT *, MAX(status) OVER (PARTITION BY gupid) AS max_status "
				+ "FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1) "
				+ "WHERE createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00') AS subquery "
				+ "WHERE status = max_status;";

		executeQuery(state, sql, blackhole);
	}

	@Benchmark
	public void testAllFieldsUsinWhere_G3(BenchmarkState state, Blackhole blackhole) {
		String sql = "SELECT COUNT(*) FROM (SELECT *, MAX(status) OVER (PARTITION BY gupid) AS max_status "
				+ "FROM jmh_sql_test.sub_index_test sit "
				+ "WHERE createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00') AS subquery "
				+ "WHERE status = max_status;";
		executeQuery(state, sql, blackhole);
	}

	private void executeQuery(BenchmarkState state, String sql, Blackhole blackhole) {
		try (Connection connection = state.dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql)) {

			while (resultSet.next()) {
				blackhole.consume(resultSet.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("SQL query failed!");
			e.printStackTrace();
		}
	}

}
