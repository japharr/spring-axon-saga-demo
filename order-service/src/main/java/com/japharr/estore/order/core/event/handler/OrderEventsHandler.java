package com.japharr.estore.order.core.event.handler;

import com.japharr.estore.order.core.event.OrderCreatedEvent;
import com.japharr.estore.order.entity.OrderEntity;
import com.japharr.estore.order.repository.OrdersRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    private final OrdersRepository ordersRepository;

    public OrderEventsHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);

        ordersRepository.save(orderEntity);
    }
}
