package com.stanzaliving.secure.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDto implements Serializable {

	private static final long serialVersionUID = 1635296868115644218L;

	protected Long id;

	protected String uuid;

	protected Date createdAt;

	protected String createdBy;

	protected Date updatedAt;

	protected String updatedBy;

	protected Boolean status;

}