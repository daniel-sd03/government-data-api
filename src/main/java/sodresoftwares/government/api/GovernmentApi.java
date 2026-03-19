package sodresoftwares.government.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GovernmentApi {

	public static void main(String[] args) {
		SpringApplication.run(GovernmentApi.class, args);
	}

}