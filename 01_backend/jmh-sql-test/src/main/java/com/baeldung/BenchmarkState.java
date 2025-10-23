package com.baeldung;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@State(Scope.Benchmark)
public class BenchmarkState {
	public HikariDataSource dataSource;

	private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/jmh_sql_test";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456";

	@Setup(Level.Trial)
	public void setup() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(JDBC_URL);
		config.setUsername(USERNAME);
		config.setPassword(PASSWORD);
		config.setMaximumPoolSize(50);
		dataSource = new HikariDataSource(config);
	}

	@TearDown(Level.Trial)
	public void tearDown() {
		dataSource.close();
	}
}
