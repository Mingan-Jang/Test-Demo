package com.event.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.clickhouse")
public class ClickHouseDataSourceProperties {

	private String jdbcUrl;
	private String username;
	private String password;
	private String driverClassName;
	private Hikari hikari;

	@Data
	public static class Hikari {
		private int maximumPoolSize;
		private int minimumIdle;
		private long idleTimeout;
		private long maxLifetime;
		private String poolName;
	}
}
