package com.japharr.estore.core.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservationCancelEvent {
    private final String productId;

    private final String orderId;
    private final String userId;
    private final int quantity;
    private final String reason;

}
