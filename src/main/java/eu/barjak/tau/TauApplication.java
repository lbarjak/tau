package eu.barjak.tau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class TauApplication {

	public static void main(String[] args) {
		SpringApplication.run(TauApplication.class, args);
	}

}
