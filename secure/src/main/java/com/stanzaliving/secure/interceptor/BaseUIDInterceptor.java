package com.stanzaliving.secure.interceptor;

import com.stanzaliving.secure.constants.StanzaConstants;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Log4j2
public class BaseUIDInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		String guid = UUID.randomUUID().toString().replace("-", ""); // globally unique identifier
		String luid = UUID.randomUUID().toString().replace("-", ""); // locally unique identifier

		guid = (null != request.getHeader(StanzaConstants.GUID)) ? request.getHeader(StanzaConstants.GUID) : guid;
		MDC.put(StanzaConstants.GUID, guid);
		MDC.put(StanzaConstants.LUID, luid);
		MDC.put(StanzaConstants.REQUEST_PATH, request.getRequestURI());
		MDC.put(StanzaConstants.QUERY_STRING, request.getQueryString());

		log.info("RequestReceived URI {} QueryString {} AppVersion {}", request.getRequestURI(), request.getQueryString(), request.getHeader("appversion"));

		request.setAttribute(StanzaConstants.GUID, guid);
		request.setAttribute(StanzaConstants.LUID, luid);

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		MDC.remove(StanzaConstants.GUID);
		MDC.remove(StanzaConstants.LUID);
		MDC.remove(StanzaConstants.REQUEST_PATH);
		MDC.remove(StanzaConstants.QUERY_STRING);
		MDC.remove(StanzaConstants.REQ_UID);
		MDC.remove(StanzaConstants.REQ_MOBILE);
	}

}