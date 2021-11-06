package com.japharr.estore.payment.core.event.handler;

import com.japharr.estore.core.event.PaymentProcessedEvent;
import com.japharr.estore.payment.entity.PaymentEntity;
import com.japharr.estore.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventHandler {
    private final PaymentRepository paymentRepository;

    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("PaymentProcessedEvent is called for orderId: {}", paymentProcessedEvent.getOrderId());

        paymentRepository.save(new PaymentEntity(
                paymentProcessedEvent.getPaymentId(),
                paymentProcessedEvent.getOrderId())
        );
    }
}
