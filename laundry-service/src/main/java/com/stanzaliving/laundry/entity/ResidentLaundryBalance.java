package com.stanzaliving.laundry.entity;

import com.stanzaliving.laundry.enums.BagType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "resident_laundry_balance")
public class ResidentLaundryBalance extends AbstractJpaEntity {

    @Column(name = "resident_id", nullable = false)
    private String residentId;

    @Column(name = "bag_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BagType bagType;

    @Column(name = "bag_purchased", nullable = false)
    private Integer bagPurchased;

    @Column(name = "bags_scanned", nullable = false)
    private Integer bagsScanned;
}
