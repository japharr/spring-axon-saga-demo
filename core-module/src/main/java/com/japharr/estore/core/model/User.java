package com.japharr.estore.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class User {
    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetail paymentDetail;
}
