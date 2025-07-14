package com.stanzaliving.secure.dto;


import com.stanzaliving.secure.enums.Department;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto extends UserDto {

	private String firstName;

	private String middleName;

	private String lastName;

	private String secondaryEmail;

	private Department department;

	private boolean secondaryEmailVerified;

	private String secondaryIsoCode;

	private String secondaryMobile;

	private boolean secondaryMobileVerified;

	private String profilePicture;

	private LocalDate birthday;

	private LocalDate anniversaryDate;

	private String nextDestination;
	
	private LocalDate arrivalDate;
	
	private int ageLimit;

	private boolean status;
}