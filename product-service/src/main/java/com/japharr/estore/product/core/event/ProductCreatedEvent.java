package com.japharr.estore.product.core.event;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ProductCreatedEvent {
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;
}
