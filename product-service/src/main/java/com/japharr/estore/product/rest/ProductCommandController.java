package com.japharr.estore.product.rest;

import com.japharr.estore.product.core.command.CreateProductCommand;
import com.japharr.estore.product.model.CreateProductModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {
    private final CommandGateway commandGateway;

    public ProductCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@RequestBody @Valid CreateProductModel product) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .title(product.getTitle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();

        String returnValue;

        returnValue = commandGateway.sendAndWait(createProductCommand);
        return returnValue;
    }
}

