package com.japharr.estore.product.core.event.handler;

//import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.japharr.estore.core.event.ProductReservationCancelEvent;
import com.japharr.estore.core.event.ProductReservedEvent;
import com.japharr.estore.product.core.event.ProductCreatedEvent;
import com.japharr.estore.product.entity.ProductEntity;
import com.japharr.estore.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {
    private final ProductRepository productRepository;

    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ExceptionHandler(resultType = Exception.class)
    private void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    private void handleIllegalArgumentException(IllegalArgumentException exception) throws IllegalArgumentException {

    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        log.info("persisting productCreatedEvent: {}", event);
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);

        productRepository.save(productEntity);
        log.info("persisting productCreatedEvent: done");
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent) throws Exception {
        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());

        log.info("ProductReservedEvent: Current product quantity " + productEntity.getQuantity());

        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
        productRepository.save(productEntity);

        log.info("ProductReservedEvent: New product quantity " + productEntity.getQuantity());

        log.info("ProductReservedEvent is called for productId:" + productReservedEvent.getProductId() +
                " and orderId: " + productReservedEvent.getOrderId());
    }

    @EventHandler
    public void on(ProductReservationCancelEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        log.info("ProductReservationCancelEvent: Current product quantity " + productEntity.getQuantity());

        productEntity.setQuantity(productEntity.getQuantity() + event.getQuantity());
        productRepository.save(productEntity);
    }
}
