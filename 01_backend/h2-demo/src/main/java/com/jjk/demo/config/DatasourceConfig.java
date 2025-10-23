package com.jjk.demo.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.h2.tools.Server;
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
		hikariConfig.setJdbcUrl("jdbc:h2:mem:baeldung;INIT=RUNSCRIPT FROM 'src/main/resources/h2init.sql'");
		hikariConfig.setUsername("testUser");
		hikariConfig.setPassword("222222");
		return new HikariDataSource(hikariConfig);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092", "-trace", "-trace_level_file",
				"3");
	}

}
