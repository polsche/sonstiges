package de.patrickolschewsky.stback.stback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StBackApplication.class, args);
	}
}
