package com.japharr.estore.order.core.event;

import com.japharr.estore.order.model.OrderStatus;
import lombok.Data;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
