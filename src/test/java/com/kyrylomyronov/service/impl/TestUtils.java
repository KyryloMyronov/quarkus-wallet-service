package com.kyrylomyronov.service.impl;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.TransactionType;
import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.exception.error.ApiError;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

	public static TransactionEntity createNewTransactionEntity(WalletEntity wallet, String transactionType) {
		TransactionEntity entity = new TransactionEntity();
		entity.setTransactionType(TransactionType.fromValue(transactionType));
		entity.setNote("Debit transaction");
		entity.setExternalId(UUID.randomUUID().toString());
		entity.setWallet(wallet);
		entity.setAmount(BigDecimal.ONE);
		entity.onCreate();
		return entity;
	}

	public static WalletEntity createNewWallet() {
		WalletEntity entity = new WalletEntity();
		entity.setBalance(BigDecimal.TEN);
		entity.setExternalId(UUID.randomUUID().toString());
		entity.setPlayerId(UUID.randomUUID().toString());
		entity.setTransactions(emptyList());
		return entity;
	}

	public static void assertException(ApiException exception, ApiError apiError) {
		assertAll(
				() -> assertEquals(exception.getCode(), apiError.getCode()),
				() -> assertEquals(exception.getStatus(), apiError.getStatus())
		);
	}
}
