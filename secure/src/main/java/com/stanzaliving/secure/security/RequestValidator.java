package com.stanzaliving.secure.security;

import com.stanzaliving.secure.dto.CurrentUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface RequestValidator {

	CurrentUser validate(HttpServletRequest request, HttpServletResponse response, CurrentUser user);
}