package com.productmodel.productmodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductmodelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductmodelApplication.class, args);
	}

}
