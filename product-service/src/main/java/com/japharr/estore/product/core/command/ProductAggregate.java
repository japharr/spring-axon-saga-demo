package com.japharr.estore.productservice.core.command;

//import com.japharr.estore.core.command.ReserveProductCommand;
//import com.japharr.estore.core.event.ProductReservedEvent;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.japharr.estore.productservice.core.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Slf4j
//@Aggregate
@Aggregate(snapshotTriggerDefinition="productSnapshotTriggerDefinition")
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

    public ProductAggregate() {}

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {
        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less than or equal to zero");
        }

        if(createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();

        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);

//        if(true) throw new Exception("An error took place in the command handler mmethod");
    }

    @CommandHandler
    public void handle(ReserveProductCommand reservedProductCommand) {
        log.info("ProductAggregate handle ReservedProductCommand: {}", reservedProductCommand);
        if(quantity < reservedProductCommand.getQuantity()) {
            log.info("ProductAggregate handle ReservedProductCommand: Insufficient number of item in stocks");
            throw new IllegalArgumentException("Insufficient number of item in stocks");
        }

        ProductReservedEvent reservedEvent = ProductReservedEvent.builder()
                .quantity(reservedProductCommand.getQuantity())
                .orderId(reservedProductCommand.getOrderId())
                .userId(reservedProductCommand.getUserId())
                .productId(reservedProductCommand.getProductId())
                .build();

        AggregateLifecycle.apply(reservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }

}
