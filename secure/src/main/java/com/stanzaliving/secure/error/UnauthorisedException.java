package com.stanzaliving.secure.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@lombok.Data
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorisedException extends RuntimeException {
	private static final long serialVersionUID = -2218378637658584346L;

	public UnauthorisedException(String message) {
		super(message);
	}

}
