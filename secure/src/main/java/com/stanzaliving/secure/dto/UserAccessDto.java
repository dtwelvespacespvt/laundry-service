package com.stanzaliving.secure.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessDto {

	@NotBlank(message = "User Id is mandatory for ACL")
	private String userId;

	@NotBlank(message = "API Url is mandatory for ACL")
	private String url;
}