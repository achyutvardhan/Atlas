package com.sendemail.sendemailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SendemailserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendemailserviceApplication.class, args);
	}

}
