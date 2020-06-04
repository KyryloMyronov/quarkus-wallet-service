package com.kyrylomyronov.api.util;

import com.kyrylomyronov.leovegastest.api.model.Transaction;
import com.kyrylomyronov.leovegastest.entity.TransactionEntity;
import com.kyrylomyronov.leovegastest.entity.WalletEntity;
import com.kyrylomyronov.leovegastest.service.impl.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static com.kyrylomyronov.leovegastest.service.impl.TestUtils.createNewTransactionEntity;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TransactionConverterTest {

	@ParameterizedTest
	@ValueSource(strings = {"credit", "debit"})
	void toApi(String transactionType) {
		WalletEntity wallet = TestUtils.createNewWallet();
		TransactionEntity entity = createNewTransactionEntity(wallet, transactionType);
		Transaction model = TransactionConverter.toApiObject(entity);

		assertAll(
				() -> assertEquals(entity.getExternalId(), model.getId()),
				() -> assertEquals(entity.getNote(), model.getNote()),
				() -> assertEquals(entity.getWallet().getExternalId(), model.getWalletId()),
				() -> assertEquals(entity.getTransactionType().toString(), model.getTransactionType()),
				() -> assertEquals(entity.getAmount().toString(), model.getAmount().toString()),
				() -> assertEquals(entity.getModified(), model.getModified())
		);
	}

	@ParameterizedTest
	@ValueSource(strings = {"credit", "debit"})
	void toEntity(String transactionType) {
		Transaction model = new Transaction();
		model.setTransactionType(transactionType);
		model.setNote("Gears Of leo");
		model.setId(UUID.randomUUID().toString());
		model.setAmount(BigDecimal.ONE);

		TransactionEntity entity = TransactionConverter.toEntity(model);

		assertAll(
				() -> assertEquals(model.getId(), entity.getExternalId()),
				() -> assertEquals(model.getNote(), entity.getNote()),
				() -> assertEquals(model.getTransactionType(), entity.getTransactionType().toString()),
				() -> assertEquals(model.getAmount().toString(), entity.getAmount().toString())
		);
	}
}
