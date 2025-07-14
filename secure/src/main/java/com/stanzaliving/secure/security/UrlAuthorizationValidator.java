package com.stanzaliving.secure.security;


import com.stanzaliving.secure.service.AuthHttpService;
import com.stanzaliving.secure.service.AuthService;
import com.stanzaliving.secure.dto.CurrentUser;
import com.stanzaliving.secure.dto.UserAccessDto;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class UrlAuthorizationValidator implements RequestValidator {

	private AuthHttpService authHttpService;
	private AuthService authService;

	public UrlAuthorizationValidator(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public CurrentUser validate(HttpServletRequest request, HttpServletResponse response, CurrentUser user) {

		log.debug("UrlAuthorizationValidator:: Validating ACL for User: " + user.getUserId());

		UserAccessDto accessDto = UserAccessDto.builder().userId(user.getUserId()).url(getValidationUri(request)).build();

		authService.checkUrlPermission(accessDto);

		return user;
	}

	private String getValidationUri(HttpServletRequest request) {

		String requestedUri = request.getHeader("uri");
		String accessUri = request.getRequestURI();

		log.debug("UrlAuthorizationValidator:: [RequestedURI: " + requestedUri + ", AccessURI: " + accessUri + ", Referrer: " + request.getHeader("referer") + "]");

		if (requestedUri != null) {
			requestedUri = requestedUri.toLowerCase();
		}

		if (accessUri != null) {
			accessUri = accessUri.toLowerCase();
		}

		if (requestedUri == null) {
			return accessUri;

		}
		return requestedUri;
	}
}