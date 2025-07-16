package com.stanzaliving.laundry.entity;

import com.stanzaliving.laundry.enums.BagType;
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
@Table(name = "resident_bag_mapping")
public class ResidentBagMapping extends AbstractJpaEntity {

    @Column(name = "resident_id", nullable = false)
    private String residentId;

    @Column(name = "bag_id", nullable = false)
    private String bagId;

    @Column(name = "bag_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BagType bagType;

    @Column(name = "date", nullable = false)
    private LocalDate scannedAt;
}
