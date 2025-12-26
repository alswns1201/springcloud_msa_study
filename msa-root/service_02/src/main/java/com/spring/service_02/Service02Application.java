package com.spring.service_02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Service02Application {

	public static void main(String[] args) {
		SpringApplication.run(Service02Application.class, args);
	}

}
