package com.japharr.estore.order.repository;

import com.japharr.estore.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository <OrderEntity, String>{
    OrderEntity findByOrderId(String orderId);
}
