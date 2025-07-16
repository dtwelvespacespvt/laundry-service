package com.stanzaliving.laundry.entity;

import com.stanzaliving.laundry.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "laundry_payment")
public class LaundryPayment extends AbstractJpaEntity {

    @Column(name = "resident_id", nullable = false)
    private String residentId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
