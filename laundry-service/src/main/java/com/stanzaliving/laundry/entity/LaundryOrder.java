package com.stanzaliving.laundry.entity;

import com.stanzaliving.laundry.enums.BagType;
import com.stanzaliving.laundry.enums.LaundryPaymentStatus;
import com.stanzaliving.laundry.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "laundry_order")
public class LaundryOrder extends AbstractJpaEntity {

    @Column(name = "resident_id", nullable = false)
    private String residentId;

    @Column(name = "residence_id", nullable = false)
    private String residenceId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "bag_id", nullable = false)
    private String bagId;

    @Column(name = "bag_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BagType bagType;

    @Column(name = "item_count")
    private Integer itemCount;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "sub_status")
    private String subStatus;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LaundryPaymentStatus paymentStatus;

    @Column(name = "free_weight")
    @Builder.Default
    private Double freeWeight = NumberUtils.DOUBLE_ZERO;

    @Column(name = "chargeable_weight")
    private Double chargeableWeight;
}
