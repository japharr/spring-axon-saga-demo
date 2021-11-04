package com.japharr.estore.user.core.query.handler;

import com.japharr.estore.core.model.PaymentDetail;
import com.japharr.estore.core.model.User;
import com.japharr.estore.core.query.FetchUserPaymentDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserQueryHandler {
    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailQuery query) {
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        return User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId(query.getUserId())
                .paymentDetail(paymentDetail)
                .build();
    }
}
