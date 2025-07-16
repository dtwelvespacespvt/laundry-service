package com.stanzaliving.laundry.repository;

import com.stanzaliving.laundry.entity.LaundryOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryOrderRepository extends AbstractJpaRepository<LaundryOrder, Long> {
}
