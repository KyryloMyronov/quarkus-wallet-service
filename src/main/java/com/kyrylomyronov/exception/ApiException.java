package com.kyrylomyronov.exception;

import com.kyrylomyronov.exception.error.ApiError;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {

	private String code;

	public ApiException(ApiError error) {
		super(error.getStatus(), error.getMessage());
		code = error.getCode();
	}

	public String getCode() {
		return code;
	}
}
