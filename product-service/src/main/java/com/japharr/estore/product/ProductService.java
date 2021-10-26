package com.japharr.estore.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductService {

	public static void main(String[] args) {
		SpringApplication.run(ProductService.class, args);
	}

}
