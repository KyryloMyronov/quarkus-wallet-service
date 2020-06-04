package com.kyrylomyronov.service.validator;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.TransactionType;
import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.kyrylomyronov.exception.error.TransactionError.*;
import static com.kyrylomyronov.exception.error.WalletError.WALLET_ID_IS_MISSING;
import static com.kyrylomyronov.service.impl.TestUtils.assertException;
import static com.kyrylomyronov.service.impl.TestUtils.createNewWallet;
import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TransactionValidatorTest {

	com.kyrylomyronov.service.validator.TransactionValidator validator;

	private TransactionEntity transaction;

	@BeforeEach
	void setup() {
		validator = new com.kyrylomyronov.service.validator.TransactionValidator();
		transaction = new TransactionEntity();
		transaction.setWallet(createNewWallet());
		transaction.setExternalId(UUID.randomUUID().toString());
		transaction.setAmount(TEN);
		transaction.setTransactionType(TransactionType.fromValue("credit"));
	}

	@Test
	void walletIdIsMissing() {
		WalletEntity wallet = createNewWallet();
		wallet.setExternalId(null);
		transaction.setWallet(wallet);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(transaction));
		assertException(exception, WALLET_ID_IS_MISSING);
	}

	@Test
	void transactionIdIsMissing() {
		transaction.setExternalId(null);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(transaction));
		assertException(exception, TRANSACTION_ID_IS_MISSING);
	}

	@Test
	void amountIsMissing() {
		transaction.setAmount(null);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(transaction));
		assertException(exception, AMOUNT_IS_MISSING);
	}

	@Test
	void transactionTypeIsMissing() {
		transaction.setTransactionType(null);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(transaction));
		assertException(exception, TRANSACTION_TYPE_IS_MISSING);
	}

	@Test
	void amountIsNegative() {
		transaction.setAmount(BigDecimal.ONE.negate());
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(transaction));
		assertException(exception, AMOUNT_IS_NEGATIVE);
	}
}