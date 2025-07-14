package com.stanzaliving.secure.service;


import com.stanzaliving.secure.dto.UserAccessDto;
import com.stanzaliving.secure.dto.UserProfileDto;
import com.stanzaliving.secure.dto.common.ResponseDto;

public interface AuthService {

    ResponseDto<UserProfileDto> validateToken(String token);

    void validateUrlPermission(String userId, String uri);

    ResponseDto<UserProfileDto> getUserProfile();
    ResponseDto<UserProfileDto> getUserByToken(String token);

    ResponseDto<Boolean> checkUrlPermission(UserAccessDto userAccessDto);
}