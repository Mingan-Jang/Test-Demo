package com.jjk.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatasourceConfig {

	@Bean(name = "datasource")
	public DataSource getDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("org.h2.Driver");
		hikariConfig.setJdbcUrl("jdbc:h2:tcp://localhost:9092/mem:baeldung");
		hikariConfig.setUsername("testUser");
		hikariConfig.setPassword("222222");
		return new HikariDataSource(hikariConfig);
	}

}
