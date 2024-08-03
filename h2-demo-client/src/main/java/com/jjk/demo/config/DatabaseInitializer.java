package com.jjk.demo.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseInitializer {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DataSource datasource;

	@PostConstruct
	public void init() {
		String sqlStatements[] = { "insert into users(id,name) values(3, 'Donald')" };

		Arrays.asList(sqlStatements).forEach(sql -> {
			try {
				logger.info("Executing SQL: {}", sql); // Log the SQL statement
				jdbcTemplate.execute(sql);
				logger.info("Executed SQL successfully: {}", sql); // Log success
			} catch (Exception e) {
				logger.error("Error executing SQL: {}", sql, e); // Log any errors
			}
		});

	}
}
