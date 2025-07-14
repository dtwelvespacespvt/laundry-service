package com.stanzaliving.secure.error;

import lombok.Getter;

@Getter
public class StanzaSecurityException extends RuntimeException {

	private static final long serialVersionUID = -3368655266237942363L;

	private final int statusCode;

	public StanzaSecurityException(String message) {
		super(message);
		this.statusCode = 403;
	}

	public StanzaSecurityException(Throwable cause) {
		super(cause);
		this.statusCode = 403;
	}

	public StanzaSecurityException(String message, Throwable cause) {
		super(message, cause);
		this.statusCode = 403;
	}

	public StanzaSecurityException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
}