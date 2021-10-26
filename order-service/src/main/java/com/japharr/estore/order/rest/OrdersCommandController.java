package com.japharr.estore.order.rest;

import com.japharr.estore.order.core.command.CreateOrderCommand;
import com.japharr.estore.order.model.OrderCreateRest;
import com.japharr.estore.order.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrdersCommandController {
    private final CommandGateway commandGateway;

    public OrdersCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@Valid @RequestBody OrderCreateRest order) {
        log.info("OrdersCommandController: createOrder: {}", order);

        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().addressId(order.getAddressId())
                .productId(order.getProductId()).userId(userId).quantity(order.getQuantity()).orderId(orderId)
                .orderStatus(OrderStatus.CREATED).build();

        String result = "";
        try {
            result = commandGateway.sendAndWait(createOrderCommand);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }
}
