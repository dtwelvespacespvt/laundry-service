package com.stanzaliving.secure.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class CustomRuntimeException extends RuntimeException {

	private HttpStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp;

	private String message;

	public CustomRuntimeException(String message, HttpStatus status) {
		this.timestamp = new Date();
		this.status = status;
		this.message = message;
	}

}
