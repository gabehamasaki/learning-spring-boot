package com.gabriel.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Learning Spring Boot Application", version = "1", description = "API for learning Spring Boot"))
public class SalesControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesControlApplication.class, args);
	}

}
