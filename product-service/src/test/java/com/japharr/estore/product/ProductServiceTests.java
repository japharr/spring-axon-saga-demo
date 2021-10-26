package com.japharr.estore.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = ("spring.cloud.discovery.enabled = false"))
class ProductServiceTests {

	@Test
	void contextLoads() {
	}

}
