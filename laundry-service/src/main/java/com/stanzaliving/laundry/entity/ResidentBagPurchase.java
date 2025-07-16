package com.stanzaliving.laundry.entity;

import com.stanzaliving.laundry.enums.BagType;
import com.stanzaliving.laundry.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "resident_bag_purchase")
public class ResidentBagPurchase extends AbstractJpaEntity {

    @Column(name = "resident_id", nullable = false)
    private String residentId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "bag_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BagType bagType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
