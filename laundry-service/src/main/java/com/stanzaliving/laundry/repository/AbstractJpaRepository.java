package com.stanzaliving.laundry.repository;

import com.stanzaliving.laundry.entity.AbstractJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AbstractJpaRepository<T extends AbstractJpaEntity, I extends Serializable> extends JpaRepository<T, I>, JpaSpecificationExecutor<T> {

}
