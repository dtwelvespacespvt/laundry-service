package com.stanzaliving.secure.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.authMechanism.AuthStrategy;
import com.stanzaliving.secure.dto.CurrentUser;
import com.stanzaliving.secure.dto.SecurityContextHolder;
import com.stanzaliving.secure.dto.ValidateTokenRequestDto;
import com.stanzaliving.secure.dto.common.ResponseDto;
import com.stanzaliving.secure.enums.AuthStrategyEnum;
import com.stanzaliving.secure.enums.ClientTypeEnum;
import com.stanzaliving.secure.error.StanzaHttpException;
import com.stanzaliving.secure.error.StanzaSecurityException;
import com.stanzaliving.secure.security.RequestValidator;
import lombok.extern.log4j.Log4j2;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Log4j2
public class AuthInterceptorV1 extends HandlerInterceptorAdapter {

    @Autowired
    @Qualifier("aesValidation")
    private AuthStrategy aesValidation;

    @Autowired
    @Qualifier("basicValidation")
    private AuthStrategy basicValidation;

    @Autowired
    @Qualifier("jwtValidation")
    private AuthStrategy jwtValidation;

    private ObjectMapper objectMapper;
    private boolean corsSupport;
    private List<RequestValidator> validations;

    public AuthInterceptorV1(@NotNull ObjectMapper objectMapper) {
        this(objectMapper, new ArrayList<>());
    }

    public AuthInterceptorV1(@NotNull ObjectMapper objectMapper, @NotNull List<RequestValidator> validations) {
        Assert.notNull(validations, "validations can't be null");
        this.objectMapper = objectMapper;
        this.validations = validations;
    }

    public void setCorsSupport(boolean corsSupport) {
        this.corsSupport = corsSupport;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if (corsSupport && HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            log.debug("CORS request!");
            return true;
        }


        try {
            log.debug("Authentication Started");

            CurrentUser user = null;

            String clientType= request.getHeader("clientType");
            String clientId= request.getHeader("clientId");
            String authMechanism= request.getHeader("authMech");
            String accessKey= request.getHeader("secret");
            if(!StringUtils.isEmpty(clientType) && !StringUtils.isEmpty(authMechanism) &&
                    !StringUtils.isEmpty(accessKey) && !StringUtils.isEmpty(clientId)) {
                ClientTypeEnum clientTypeEnum = ClientTypeEnum.fromClientType(clientType);
                AuthStrategyEnum authStrategyEnum = AuthStrategyEnum.fromAuthStrategy(authMechanism);
                if (Arrays.asList(ClientTypeEnum.S2S, ClientTypeEnum.THIRD_PARTY).contains(clientTypeEnum)) {
                    ValidateTokenRequestDto validateTokenRequestDto = ValidateTokenRequestDto.builder().secretToken(accessKey).userId(clientId).authStrategyEnum(authStrategyEnum).build();
                    boolean validated = false;
                    switch (authStrategyEnum) {
                        case AES:
                            validated = aesValidation.validateToken(validateTokenRequestDto);
                            break;
                        case JWT:
                            validated = jwtValidation.validateToken(validateTokenRequestDto);
                            break;
                        case BASIC:
                            validated = basicValidation.validateToken(validateTokenRequestDto);
                            break;
                    }
                    if (validated) {
                        MDC.put("ServiceType", CurrentUser.builder().userId(clientId).token(accessKey).build());
                        return true;
                    } else {
                        throw new StanzaSecurityException("Invalid token or User Session has expired", HttpStatus.UNAUTHORIZED.value());
                    }
                }
            }

            for (RequestValidator validator : validations) {
                user = validator.validate(request, response, user);
            }

            if (Objects.nonNull(user)) {
                SecurityContextHolder.setCurrentUser(user);
            }

            return true;

        } catch (StanzaHttpException ex) {
            log.error("Found StanzaHttpException: {}", ex.getMessage());
            ResponseDto<Void> res = ResponseDto.failure("We are facing some internal issue, please try after sometime.");

            if (ex.getStatusCode() > 0) {
                response.setStatus(ex.getStatusCode());
                res.setMessage(ex.getMessage());
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            setResponse(request, response, res);

        } catch (StanzaSecurityException ex) {
            log.error("Found StanzaSecurityException: {}", ex.getMessage());
            returnError(request, response, ex);
        }

        return false;
    }

    private void returnError(HttpServletRequest request, HttpServletResponse response, StanzaSecurityException ex) throws IOException {

        String message = ex != null ? ex.getMessage() : "Token Is Invalid";

        ResponseDto<Void> res = ResponseDto.failure(message);

        if (ex != null) {
            response.setStatus(ex.getStatusCode());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        setResponse(request, response, res);
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response, ResponseDto<Void> res) throws JsonProcessingException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            writer.print(objectMapper.writeValueAsString(res));
        }
    }
}
