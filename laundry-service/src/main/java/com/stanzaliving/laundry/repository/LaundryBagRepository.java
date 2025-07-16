package com.stanzaliving.laundry.repository;

import com.stanzaliving.laundry.entity.LaundryBag;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryBagRepository extends AbstractJpaRepository<LaundryBag, Long> {
}
