package com.stanzaliving.laundry.repository;

import com.stanzaliving.laundry.entity.LaundryPayment;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryPaymentRepository extends AbstractJpaRepository<LaundryPayment, Long> {
}
