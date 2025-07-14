package com.stanzaliving.secure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailDto {
    @JsonProperty("employeeId")
    private String employeeId;

    @JsonProperty("userUuid")
    private String userUuid;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("designation")
    private Object designation;

    @JsonProperty("maritalStatus")
    private Object maritalStatus;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("bloodGroup")
    private Object bloodGroup;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("gender")
    private Object gender;

    @JsonProperty("isoCode")
    private Object isoCode;

    @JsonProperty("userProfileDto")
    private UserProfileDetails userProfileDto;

    @JsonProperty("userAttributesDto")
    private Object userAttributesDto;

    @JsonProperty("roleAssignmentDtos")
    private Object roleAssignmentDtos;

    @JsonProperty("roleDtos")
    private List<RoleDto> roleDtos;

}
