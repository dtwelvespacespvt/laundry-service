package com.stanzaliving.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.interceptor.AuthInterceptorV1;
import com.stanzaliving.secure.security.RequestValidator;
import com.stanzaliving.secure.security.TokenAuthenticationValidator;
import com.stanzaliving.secure.security.UrlAuthorizationValidator;
import com.stanzaliving.secure.service.AuthService;
import com.stanzaliving.secure.service.impl.AuthServiceImpl;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class SecurityModuleBuilderV1 {

    private ObjectMapper objectMapper;

    private boolean corsSupport = false;

    private String userServiceUrl;

    private boolean enableTokenBasedAuthentication = false;

    private boolean enableUrlBasedAuthorization = false;

    private List<RequestValidator> validators = new ArrayList<>();

    public SecurityModuleBuilderV1 objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public SecurityModuleBuilderV1 corsSupport(boolean corsSupport) {
        this.corsSupport = corsSupport;
        return this;
    }

    public SecurityModuleBuilderV1 userServiceUrl(String userServiceUrl) {
        this.userServiceUrl = userServiceUrl;
        return this;
    }

    public SecurityModuleBuilderV1 enableTokenBasedAuthentication(boolean enableTokenBasedAuthentication) {
        this.enableTokenBasedAuthentication = enableTokenBasedAuthentication;
        return this;
    }

    public SecurityModuleBuilderV1 enableUrlBasedAuthorization(boolean enableUrlBasedAuthorization) {
        this.enableUrlBasedAuthorization = enableUrlBasedAuthorization;
        return this;
    }

    public SecurityModuleBuilderV1 addValidator(RequestValidator validator) {
        Assert.notNull(validator, "validator can't be null");

        validators.add(validator);
        return this;
    }

    public AuthInterceptorV1 build() {
        Assert.notNull(objectMapper, "Object Mapper can't be null");
        Assert.hasText(userServiceUrl, "User Service URL can't be null");

        AuthService authService = new AuthServiceImpl(userServiceUrl);

        if (enableTokenBasedAuthentication) {
            validators.add(0, new TokenAuthenticationValidator(authService));

            if (enableUrlBasedAuthorization) {
                validators.add(1, new UrlAuthorizationValidator(authService));
            }
        }

        AuthInterceptorV1 authInterceptor = new AuthInterceptorV1(objectMapper, validators);
        authInterceptor.setCorsSupport(corsSupport);

        return authInterceptor;

    }
}
