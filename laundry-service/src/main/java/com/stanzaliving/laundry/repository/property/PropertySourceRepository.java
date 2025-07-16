package com.stanzaliving.laundry.repository.property;

import com.stanzaliving.laundry.entity.property.PropertySource;
import com.stanzaliving.laundry.repository.AbstractJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertySourceRepository extends AbstractJpaRepository<PropertySource, Long> {

    Optional<PropertySource> findByKeyAndStatus(String key, boolean status);
}
