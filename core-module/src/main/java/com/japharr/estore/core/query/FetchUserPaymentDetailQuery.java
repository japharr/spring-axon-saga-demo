package com.japharr.estore.core.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchUserPaymentDetailQuery {
    private String userId;
}
