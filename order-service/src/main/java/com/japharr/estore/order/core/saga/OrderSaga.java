package com.japharr.estore.order.core.saga;

import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.japharr.estore.order.core.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty="orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {

        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();


        log.info("OrderSaga: OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() +
                " and productId: " + reserveProductCommand.getProductId() );

        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

            @Override
            public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
                                 CommandResultMessage<? extends Object> commandResultMessage) {
                if(commandResultMessage.isExceptional()) {
                    // Start a compensating transaction
                    log.info("OrderSaga: An error occurred: {}", commandResultMessage.exceptionResult().getLocalizedMessage());
                }

            }

        });
    }

    @SagaEventHandler(associationProperty="orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("OrderSaga: ProductReservedEvent: {}", productReservedEvent.getProductId());
    }
}
