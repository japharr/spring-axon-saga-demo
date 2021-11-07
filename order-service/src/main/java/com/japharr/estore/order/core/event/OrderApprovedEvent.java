package com.japharr.estore.order.core.event;

import com.japharr.estore.order.model.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
    String orderId;
    OrderStatus orderStatus = OrderStatus.APPROVED;
}
