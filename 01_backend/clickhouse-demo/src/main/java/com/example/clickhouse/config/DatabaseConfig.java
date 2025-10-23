package com.example.clickhouse.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {

	// ===== PostgreSQL Configuration =====
	@Bean
	@Qualifier("postgresDataSourceProperties")
	DataSourceProperties postgresDataSourceProperties() {
		DataSourceProperties properties = new DataSourceProperties();
		properties.setUrl("jdbc:postgresql://localhost:5432/" + System.getenv("POSTGRES_DB"));
		properties.setUsername(System.getenv("POSTGRES_USER"));
		properties.setPassword(System.getenv("POSTGRES_PASSWORD"));
		return properties;
	}

	@Bean
	@Qualifier("postgresDataSource")
	DataSource postgresDataSource(@Qualifier("postgresDataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Qualifier("postgresJdbcTemplate")
	JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	// ===== ClickHouse Configuration =====
	@Bean
	@Qualifier("clickhouseDataSourceProperties")
	DataSourceProperties clickhouseDataSourceProperties() {
		DataSourceProperties properties = new DataSourceProperties();
		properties.setUrl("jdbc:clickhouse://localhost:8123/default");
		properties.setUsername(System.getenv("CLICKHOUSE_USER"));
		properties.setPassword(System.getenv("CLICKHOUSE_PASSWORD"));
		return properties;
	}

	@Bean
	@Qualifier("clickhouseDataSource")
	DataSource clickhouseDataSource(@Qualifier("clickhouseDataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Qualifier("clickhouseJdbcTemplate")
	JdbcTemplate clickhouseJdbcTemplate(@Qualifier("clickhouseDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}