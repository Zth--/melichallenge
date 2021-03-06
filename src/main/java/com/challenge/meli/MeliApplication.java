package com.challenge.meli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class MeliApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeliApplication.class, args);
	}

}
