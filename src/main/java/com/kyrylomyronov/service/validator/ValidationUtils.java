package com.kyrylomyronov.service.validator;

import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.exception.error.ApiError;

public class ValidationUtils {

	private ValidationUtils() {
		//Utils class that doesn't require an instance.
	}

	public static void validatePresentAndValid(String value, ApiError error) {
		if (value == null || value.trim().isEmpty()) {
			reject(error);
		}
	}

	public static void reject(ApiError error) {
		throw new ApiException(error);
	}
}
