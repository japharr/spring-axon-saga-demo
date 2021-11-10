package com.japharr.estore.order.core.saga;

//import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
//import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.japharr.estore.core.command.CancelProductReservationCommand;
import com.japharr.estore.core.command.ProcessPaymentCommand;
import com.japharr.estore.core.command.ReserveProductCommand;
import com.japharr.estore.core.event.PaymentProcessedEvent;
import com.japharr.estore.core.event.ProductReservationCancelEvent;
import com.japharr.estore.core.event.ProductReservedEvent;
import com.japharr.estore.core.model.User;
import com.japharr.estore.core.query.FetchUserPaymentDetailQuery;
import com.japharr.estore.order.core.command.ApproveOrderCommand;
import com.japharr.estore.order.core.command.RejectOrderCommand;
import com.japharr.estore.order.core.event.OrderApprovedEvent;
import com.japharr.estore.order.core.event.OrderCreatedEvent;
import com.japharr.estore.order.core.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        System.out.println("OrderSaga: OrderCreatedEvent handled for orderId: " + orderCreatedEvent.getOrderId() +
                " and productId: " + orderCreatedEvent.getProductId() );
        log.info("OrderSaga: OrderCreatedEvent handled for orderId: " + orderCreatedEvent.getOrderId() +
                " and productId: " + orderCreatedEvent.getProductId() );

        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();


        System.out.println("OrderSaga: ReserveProductCommand handled for orderId: " + reserveProductCommand.getOrderId() +
                " and productId: " + reserveProductCommand.getProductId() );
        log.info("OrderSaga: ReserveProductCommand handled for orderId: " + reserveProductCommand.getOrderId() +
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

    private void cancelProductReservation(ProductReservedEvent event, String reason) {
        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("OrderSaga: ProductReservedEvent: {}", productReservedEvent.getProductId());

        FetchUserPaymentDetailQuery fetchUserPaymentDetailQuery =
                new FetchUserPaymentDetailQuery(productReservedEvent.getUserId());

        User userPaymentDetail = null;

        try {
            userPaymentDetail = queryGateway.query(fetchUserPaymentDetailQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            cancelProductReservation(productReservedEvent, ex.getLocalizedMessage());
            return;
        }

        if(userPaymentDetail == null) {
            // TODO:: Start Compensating transaction
            cancelProductReservation(productReservedEvent, "Unable to fetch user-details");
            return;
        }

        log.info("OrderSaga: ProductReservedEvent: UserDetails: {}", userPaymentDetail);

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetail(userPaymentDetail.getPaymentDetail())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            cancelProductReservation(productReservedEvent, ex.getLocalizedMessage());
            return;
        }

        if(result == null) {
            log.info("result is null, initiating a compensating transaction");
            // TODO:: Start Compensating transaction
            cancelProductReservation(productReservedEvent, "Could not provide user detail");
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handlePaymentProcessedEvent(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("paymentProcessedEvent");
        commandGateway.send(new ApproveOrderCommand(paymentProcessedEvent.getOrderId()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("orderApprovedEvent, order saga complete for orderId: {}", orderApprovedEvent.getOrderId());

        //SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelEvent event) {
        log.info("OrderSaga: send reject order command");
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(
                event.getOrderId(), event.getReason()
        );

        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent event) {
        log.info("OrderSaga: order of id: {}, rejected successfully", event.getOrderId());
    }
}
