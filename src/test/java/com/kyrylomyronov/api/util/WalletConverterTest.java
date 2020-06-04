package com.kyrylomyronov.api.util;

import com.kyrylomyronov.leovegastest.api.model.Wallet;
import com.kyrylomyronov.leovegastest.entity.WalletEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.kyrylomyronov.leovegastest.service.impl.TestUtils.createNewWallet;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WalletConverterTest {

	@Test
	void toApi() {
		WalletEntity entity = createNewWallet();
		Wallet model = WalletConverter.toApiObject(entity);
		assertAll(
				() -> assertEquals(entity.getExternalId(), model.getId()),
				() -> assertEquals(entity.getPlayerId(), model.getPlayerId()),
				() -> assertEquals(entity.getBalance().toString(), model.getBalance().toString()),
				() -> assertEquals(entity.getModified(), model.getModified())
		);
	}

	@Test
	void toEntity() {
		Wallet model = new Wallet();
		model.setId(UUID.randomUUID().toString());
		model.setBalance(BigDecimal.TEN);
		model.setPlayerId(UUID.randomUUID().toString());
		WalletEntity entity = WalletConverter.toEntity(model);

		assertAll(
				() -> assertEquals(model.getId(), entity.getExternalId()),
				() -> assertEquals(model.getPlayerId(), entity.getPlayerId()),
				() -> assertEquals(model.getBalance().toString(), entity.getBalance().toString())
		);
	}
}
