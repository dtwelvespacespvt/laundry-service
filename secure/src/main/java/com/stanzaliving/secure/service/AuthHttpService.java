package com.stanzaliving.secure.service;


import com.stanzaliving.secure.dto.UserAccessDto;
import com.stanzaliving.secure.dto.UserDeptLevelRoleNameUrlExpandedDto;
import com.stanzaliving.secure.dto.UserProfileDetails;
import com.stanzaliving.secure.dto.common.ResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AuthHttpService {

    @RequestMapping(method = RequestMethod.GET, value = "/profile/current")
    ResponseDto<UserProfileDetails> getUserByToken(@RequestHeader("cookie") String token);

    @PostMapping(value = "/acl/check")
    Boolean checkUrlPermission(UserAccessDto userAccessDto);

    @RequestMapping(method = RequestMethod.GET, value = "/acl/user/be/{userUuid}")
    ResponseDto<List<UserDeptLevelRoleNameUrlExpandedDto>> getUserRolesBe(@PathVariable("userUuid") String userUuid);


}
