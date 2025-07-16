package com.stanzaliving.laundry.repository;

import com.stanzaliving.laundry.entity.ResidentLaundryBalance;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentLaundryBalanceRepository extends AbstractJpaRepository<ResidentLaundryBalance, Long> {
}
