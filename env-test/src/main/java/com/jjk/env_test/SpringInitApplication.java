package com.jjk.env_test;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SpringInitApplication {

	 public static void main(String[] args) {
	        ConfigurableApplicationContext context = SpringApplication.run(SpringInitApplication.class, args);
	        
	        System.out.println("Command-line arguments: " + Arrays.toString(args));
	        Runnable componentRunner = context.getBean(Runnable.class);
	        componentRunner.run();
			System.exit(SpringApplication.exit(context));
	    }
}
