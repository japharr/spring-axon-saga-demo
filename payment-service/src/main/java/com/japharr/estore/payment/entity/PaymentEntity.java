package com.japharr.estore.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    @Id
    private String paymentId;
    @Column
    public String orderId;
}
