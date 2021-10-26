package com.japharr.estore.order.core.command;

import com.japharr.estore.order.model.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
@ToString
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    public final String orderId;

    private final String userId;
    private final String productId;
    private final int quantity;
    private final String addressId;
    private final OrderStatus orderStatus;
}
