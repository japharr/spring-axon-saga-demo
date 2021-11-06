package com.japharr.estore.payment.core.command;

import com.japharr.estore.core.command.ProcessPaymentCommand;
import com.japharr.estore.core.event.PaymentProcessedEvent;
import com.japharr.estore.core.model.PaymentDetail;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Slf4j
@Aggregate
public class PaymentAggregate {
    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;

    public PaymentAggregate() {}

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
        if(processPaymentCommand.getPaymentDetail() == null) {
            throw new IllegalArgumentException("Missing payment details");
        }

        if(processPaymentCommand.getOrderId() == null) {
            throw new IllegalArgumentException("Missing orderId");
        }

        if(processPaymentCommand.getPaymentId() == null) {
            throw new IllegalArgumentException("Missing paymentId");
        }

        AggregateLifecycle.apply(new PaymentProcessedEvent(
                processPaymentCommand.getOrderId(),
                processPaymentCommand.getPaymentId()
        ));
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        this.orderId = paymentProcessedEvent.getOrderId();
        this.paymentId = paymentProcessedEvent.getPaymentId();
    }
}
