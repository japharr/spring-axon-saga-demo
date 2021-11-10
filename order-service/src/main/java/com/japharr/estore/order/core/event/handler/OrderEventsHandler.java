package com.japharr.estore.order.core.event.handler;

import com.japharr.estore.order.core.event.OrderApprovedEvent;
import com.japharr.estore.order.core.event.OrderCreatedEvent;
import com.japharr.estore.order.core.event.OrderRejectedEvent;
import com.japharr.estore.order.entity.OrderEntity;
import com.japharr.estore.order.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    private final OrdersRepository ordersRepository;

    public OrderEventsHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        log.info("OrderEventsHandler: OrderCreatedEvent: {}", event);

        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);

        ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApprovedEvent event) throws Exception {
        log.info("OrderEventsHandler: OrderApprovedEvent: {}", event);
        OrderEntity orderEntity = ordersRepository.findByOrderId(event.getOrderId());
        if(orderEntity == null) {
            log.info("orderEntity is null");
            return;
        }

        orderEntity.setOrderStatus(event.getOrderStatus());
        ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        log.info("OrderEventsHandler: OrderApprovedEvent: {}", event);
        OrderEntity orderEntity = ordersRepository.findByOrderId(event.getOrderId());
        if(orderEntity == null) {
            log.info("orderEntity is null");
            return;
        }

        orderEntity.setOrderStatus(event.getOrderStatus());
        ordersRepository.save(orderEntity);
    }
}
