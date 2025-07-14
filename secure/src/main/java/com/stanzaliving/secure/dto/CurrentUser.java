package com.stanzaliving.secure.dto;


import com.stanzaliving.secure.enums.Department;
import com.stanzaliving.secure.enums.UserType;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {

	private String token;

	private String userId;

	private String userUuid;
	
	private String firstName;

	private String lastName;

	private String mobile;

	private String isoCode;

	private String email;
	
	private UserType userType;
	
	private Department department;

}