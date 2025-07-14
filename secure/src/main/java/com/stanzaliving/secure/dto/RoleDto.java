package com.stanzaliving.secure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {

    private Long id;

    private String roleUuid;

    private String roleName;

    private String roleDescription;

    private List<RolePermissionDto> rolePermissionDtos;

}
