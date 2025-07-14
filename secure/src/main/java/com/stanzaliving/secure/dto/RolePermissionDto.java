package com.stanzaliving.secure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePermissionDto {

    private Long rolePermissionId;

    private Long resourcePermissionId;

    private String resource;

    private String resourcePermissionName;
}
