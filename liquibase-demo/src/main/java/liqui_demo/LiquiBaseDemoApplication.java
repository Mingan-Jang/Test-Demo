package liqui_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class LiquiBaseDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiquiBaseDemoApplication.class, args);
	}

}
