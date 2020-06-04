package com.kyrylomyronov.exception.error;

import org.springframework.http.HttpStatus;

public interface ApiError {
	String getMessage();

	HttpStatus getStatus();

	String getCode();
}
