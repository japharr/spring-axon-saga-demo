package com.japharr.estore.product.core.event.handler;


import com.japharr.estore.product.core.event.ProductCreatedEvent;
import com.japharr.estore.product.entity.ProductLookupEntity;
import com.japharr.estore.product.repository.ProductLookRepository;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@AllArgsConstructor
public class ProductLookupEventHandler {
    private final ProductLookRepository productLookRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductLookupEntity productLookupEntity =
                new ProductLookupEntity(productCreatedEvent.getProductId(), productCreatedEvent.getTitle());

        productLookRepository.save(productLookupEntity);
    }
}
