package com.travelbuddy.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;





@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Meine API",
		version = "1.0",
		description = "Beschreibung meiner API"
))
@Server(url = "http://localhost:8080")
public class  DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}


