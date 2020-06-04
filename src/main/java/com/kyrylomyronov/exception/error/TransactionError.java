package com.kyrylomyronov.exception.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

public enum TransactionError implements ApiError {
	AMOUNT_IS_MISSING(BAD_REQUEST, "Amount is missing."),
	AMOUNT_IS_NEGATIVE(BAD_REQUEST, "Amount is a negative number"),
	TRANSACTION_ID_IS_MISSING(BAD_REQUEST, "Transaction Id is missing."),
	TRANSACTION_ID_IS_NOT_UNIQUE(CONFLICT, "TransactionId should be unique."),
	TRANSACTION_NOT_FOUND(NOT_FOUND, "Transaction with provided id is not found."),
	TRANSACTION_TYPE_IS_MISSING(BAD_REQUEST, "Transaction type is missing."),
	WRONG_TRANSACTION_TYPE(BAD_REQUEST, "Wrong transaction type.");

	private final HttpStatus status;
	private final String message;

	TransactionError(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return name();
	}
}
