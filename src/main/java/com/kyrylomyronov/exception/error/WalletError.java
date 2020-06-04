package com.kyrylomyronov.exception.error;

import org.springframework.http.HttpStatus;

public enum WalletError implements ApiError {

	BALANCE_IS_MISSING(HttpStatus.BAD_REQUEST, "Balance should not be empty."),
	NOT_ENOUGH_FUNDS(HttpStatus.BAD_REQUEST, "Balance is not enough to perform debit transaction."),
	PLAYER_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "Player does not exist."),
	PLAYER_ID_IS_MISSING(HttpStatus.BAD_REQUEST, "Player Id is missing."),
	WALLET_EXISTS(HttpStatus.BAD_REQUEST, "Wallet with provided ID exists in the system."),
	WALLET_ID_IS_MISSING(HttpStatus.BAD_REQUEST, "Wallet Id is missing."),
	WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, "Wallet is not found.");

	private final HttpStatus status;
	private final String message;

	WalletError(HttpStatus status, String message) {
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
