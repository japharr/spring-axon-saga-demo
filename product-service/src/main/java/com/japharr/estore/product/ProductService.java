package com.japharr.estore.product;

import com.japharr.estore.product.core.command.interceptor.CreateProductCommandInterceptor;
import com.japharr.estore.product.core.exception.ProductServiceEventErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductService {

	public static void main(String[] args) {
		SpringApplication.run(ProductService.class, args);
	}

	@Autowired
	public void registerProductCommandInterceptor(
			ApplicationContext applicationContext, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptor.class));
	}

	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group", conf -> new ProductServiceEventErrorHandler());
		//config.registerListenerInvocationErrorHandler("product-group", conf -> PropagatingErrorHandler.instance())
	}
}
