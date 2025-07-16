package com.stanzaliving.laundry.repository;

import com.stanzaliving.laundry.entity.ResidentBagMapping;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentBagMappingRepository extends AbstractJpaRepository<ResidentBagMapping, Long> {
}
