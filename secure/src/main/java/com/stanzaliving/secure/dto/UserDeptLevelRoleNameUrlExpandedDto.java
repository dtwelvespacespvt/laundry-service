package com.stanzaliving.secure.dto;


import com.stanzaliving.secure.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDeptLevelRoleNameUrlExpandedDto {

    private String userUuid;

    private Department department;

    private AccessLevel accessLevel;

    private List<String> accessLevelEntityListUuid;

    private Map<String, String> accessLevelEntityNameUuidMap;

    private List<String> rolesList;

    private Map<String, String> roleNameUuidMap;

    private List<String> urlList;

}
