package com.stanzaliving.secure.error;

import lombok.Getter;

@Getter
public class StanzaHttpException extends RuntimeException {

	private static final long serialVersionUID = -3368655266237942363L;

	private int statusCode;

	public StanzaHttpException(String message) {
		super(message);
	}

	public StanzaHttpException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public StanzaHttpException(Throwable cause) {
		super(cause);
	}

	public StanzaHttpException(String message, Throwable cause) {
		super(message, cause);
	}

	public StanzaHttpException(String message, int statusCode, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}
}