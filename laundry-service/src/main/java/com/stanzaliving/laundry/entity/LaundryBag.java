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
@Table(name = "laundry_bag")
public class LaundryBag extends AbstractJpaEntity {

    @Column(name = "bag_id", nullable = false)
    private String bagId;

    @Column(name = "bag_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BagType bagType;

    @Column(name = "active")
    private boolean active;
}
