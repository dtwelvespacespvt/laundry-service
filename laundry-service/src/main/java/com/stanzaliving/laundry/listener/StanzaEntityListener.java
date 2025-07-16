package com.stanzaliving.laundry.listener;

import com.stanzaliving.laundry.entity.AbstractJpaEntity;
import com.stanzaliving.laundry.util.SecurityUtil;
import com.stanzaliving.laundry.util.StanzaUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Entity listener for all Entities/Models
 *
 */
public class StanzaEntityListener {

	// This code will be executed before every insert into DB
	@PrePersist
	private void beforeInsert(AbstractJpaEntity abstractEntity) {
		if (abstractEntity.getCreatedAt() == null) {
			abstractEntity.setCreatedAt(new Date());
		}
		if (abstractEntity.getUpdatedAt() == null) {
			abstractEntity.setUpdatedAt(new Date());
		}

		if (StringUtils.isBlank(abstractEntity.getUuid())) {
			abstractEntity.setUuid(StanzaUtils.generateUniqueId());
		}

		if (StringUtils.isBlank(abstractEntity.getCreatedBy())) {
			abstractEntity.setCreatedBy(SecurityUtil.getCurrentUserId());
		}

		if (StringUtils.isBlank(abstractEntity.getUpdatedBy())) {
			abstractEntity.setUpdatedBy(SecurityUtil.getCurrentUserId());
		}
	}

	// This code will be executed before every update into DB
	@PreUpdate
	private void beforeUpdate(AbstractJpaEntity abstractEntity) {
		if (abstractEntity.getCreatedAt() == null) {
			abstractEntity.setCreatedAt(new Date());
		}

		if (StringUtils.isNotBlank(SecurityUtil.getCurrentUserId())) {
			abstractEntity.setUpdatedBy(SecurityUtil.getCurrentUserId());
		}

		abstractEntity.setUpdatedAt(new Date());
	}
}