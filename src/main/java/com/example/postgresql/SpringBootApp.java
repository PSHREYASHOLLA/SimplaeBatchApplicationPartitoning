package com.example.postgresql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.example.postgresql"})
public class SpringBootApp{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
	}

}
