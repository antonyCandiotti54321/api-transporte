package com.antonycandiotti.api_transporte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ApiTransporteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTransporteApplication.class, args);
	}

}
