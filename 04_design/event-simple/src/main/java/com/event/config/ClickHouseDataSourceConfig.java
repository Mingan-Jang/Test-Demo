package com.event.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ClickHouseDataSourceConfig {

	@Bean(name = "clickHouseDataSource")
	DataSource clickHouseDataSource(ClickHouseDataSourceProperties props) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(props.getJdbcUrl());
		config.setUsername(props.getUsername());
		config.setPassword(props.getPassword());
		config.setDriverClassName(props.getDriverClassName());

		ClickHouseDataSourceProperties.Hikari hikari = props.getHikari();
		config.setMaximumPoolSize(hikari.getMaximumPoolSize());
		config.setMinimumIdle(hikari.getMinimumIdle());
		config.setIdleTimeout(hikari.getIdleTimeout());
		config.setMaxLifetime(hikari.getMaxLifetime());
		config.setPoolName(hikari.getPoolName());

		return new HikariDataSource(config);
	}

	@Bean(name = "clickHouseJdbcTemplate")
	JdbcTemplate jdbcTemplate(@Qualifier("clickHouseDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
