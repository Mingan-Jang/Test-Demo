package mapstruct_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class MapStructDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapStructDemoApplication.class, args);
	}

}
