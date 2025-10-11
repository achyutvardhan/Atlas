package com.cartmodel.cartmodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CartmodelApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartmodelApplication.class, args);
	}

}
