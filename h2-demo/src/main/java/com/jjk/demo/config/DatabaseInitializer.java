package com.jjk.demo.config;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseInitializer {
	@Autowired
	DataSource datasource;

	@PostConstruct
	public void init() {
		try (Connection connection = datasource.getConnection(); Statement statement = connection.createStatement()) {
			
			statement.execute("INSERT INTO schedule  (job) VALUES ('value1')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
