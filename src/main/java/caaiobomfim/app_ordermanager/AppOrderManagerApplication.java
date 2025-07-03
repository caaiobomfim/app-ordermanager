package caaiobomfim.app_ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppOrderManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppOrderManagerApplication.class, args);
	}

}
