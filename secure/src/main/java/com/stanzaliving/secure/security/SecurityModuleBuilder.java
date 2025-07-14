package com.stanzaliving.secure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.service.AuthService;
import com.stanzaliving.secure.service.impl.AuthServiceImpl;
import com.stanzaliving.secure.interceptor.AuthInterceptor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class SecurityModuleBuilder {

	private ObjectMapper objectMapper;

	private boolean corsSupport = false;

	private String userServiceUrl;

	private boolean enableTokenBasedAuthentication = false;

	private boolean enableUrlBasedAuthorization = false;

	private List<RequestValidator> validators = new ArrayList<>();


//	public SecurityModuleBuilder authHttpService(AuthService authService) {
//		this.authService = authService;
//		return this;
//	}

	public SecurityModuleBuilder objectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		return this;
	}

	public SecurityModuleBuilder corsSupport(boolean corsSupport) {
		this.corsSupport = corsSupport;
		return this;
	}

	public SecurityModuleBuilder userServiceUrl(String userServiceUrl) {
		this.userServiceUrl = userServiceUrl;
		return this;
	}

	public SecurityModuleBuilder enableTokenBasedAuthentication(boolean enableTokenBasedAuthentication) {
		this.enableTokenBasedAuthentication = enableTokenBasedAuthentication;
		return this;
	}

	public SecurityModuleBuilder enableUrlBasedAuthorization(boolean enableUrlBasedAuthorization) {
		this.enableUrlBasedAuthorization = enableUrlBasedAuthorization;
		return this;
	}

	public SecurityModuleBuilder addValidator(RequestValidator validator) {
		Assert.notNull(validator, "validator can't be null");

		validators.add(validator);
		return this;
	}

	public AuthInterceptor build() {
		Assert.notNull(objectMapper, "Object Mapper can't be null");
		Assert.hasText(userServiceUrl, "User Service URL can't be null");

		AuthService authService = new AuthServiceImpl(userServiceUrl);
		if (enableTokenBasedAuthentication) {
			validators.add(0, new TokenAuthenticationValidator(authService));

			if (enableUrlBasedAuthorization) {
				validators.add(1, new UrlAuthorizationValidator(authService));
			}
		}

		AuthInterceptor authInterceptor = new AuthInterceptor(objectMapper, validators);
		authInterceptor.setCorsSupport(corsSupport);

		return authInterceptor;

	}

}