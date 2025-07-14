package com.stanzaliving.secure.service.impl;


import com.stanzaliving.secure.dto.SecurityContextHolder;
import com.stanzaliving.secure.dto.UserAccessDto;
import com.stanzaliving.secure.dto.UserProfileDetails;
import com.stanzaliving.secure.dto.UserProfileDto;
import com.stanzaliving.secure.dto.common.ResponseDto;
import com.stanzaliving.secure.error.StanzaHttpException;
import com.stanzaliving.secure.error.StanzaSecurityException;
import com.stanzaliving.secure.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Log4j2
public class AuthServiceImpl implements AuthService {

    private AuthClientApi authClientApi;

    public AuthServiceImpl(String basePath) {
        authClientApi = new AuthClientApi(new StanzaRestClient(basePath));
    }

    @Override
    public ResponseDto<UserProfileDto> getUserProfile() {
        return getUserByToken(SecurityContextHolder.getCurrentUser().getToken());
    }

    @Override
    public ResponseDto<UserProfileDto> validateToken(String token) {
        return getUserByToken(token);
    }

    public ResponseDto<UserProfileDto> getUserByToken(String token) {

        ResponseDto<UserProfileDto> responseDto = null;

        try {
            responseDto = authClientApi.getCurrentUserByToken(token);
        } catch (StanzaSecurityException
                 | StanzaHttpException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting user by token: ", e);
        }

        return responseDto;
    }

    @Override
    public void validateUrlPermission(String userId, String uri) {

        ResponseDto<Boolean> responseDto = checkUrlPermission(userId, uri);

        if (responseDto == null || !responseDto.getData()) {
            throw new StanzaSecurityException("You are not allowed to access this url", HttpStatus.FORBIDDEN.value());
        }

    }

    public ResponseDto<Boolean> checkUrlPermission(UserAccessDto userAccessDto) {

        ResponseDto<Boolean> responseDto = null;
        String userId=userAccessDto.getUserId();
        String url=userAccessDto.getUserId();
        try {
            responseDto = authClientApi.checkUrlPermission(userId, url);
        } catch (StanzaSecurityException
                 | StanzaHttpException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error checking user permission for url: ", e);
        }

        return responseDto;
    }
    public ResponseDto<Boolean> checkUrlPermission(String userId, String url) {

        ResponseDto<Boolean> responseDto = null;
        try {
            responseDto = authClientApi.checkUrlPermission(userId, url);
        } catch (StanzaSecurityException
                 | StanzaHttpException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error checking user permission for url: ", e);
        }

        return responseDto;
    }
}