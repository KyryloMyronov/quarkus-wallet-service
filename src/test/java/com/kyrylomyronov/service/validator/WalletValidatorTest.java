package com.kyrylomyronov.service.validator;

import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.service.PlayerApiMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.kyrylomyronov.exception.error.WalletError.*;
import static com.kyrylomyronov.service.impl.TestUtils.assertException;
import static com.kyrylomyronov.service.impl.TestUtils.createNewWallet;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletValidatorTest {

	private com.kyrylomyronov.service.validator.WalletValidator validator;
	private WalletEntity wallet;

	@BeforeEach
	void setup() {
		PlayerApiMock playerApi = new PlayerApiMock();
		validator = new com.kyrylomyronov.service.validator.WalletValidator(playerApi);
		wallet = createNewWallet();
		playerApi.newPlayer(wallet.getPlayerId());
	}

	@Test
	void walletIdIsMissing() {
		wallet.setExternalId(null);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(wallet));
		assertException(exception, WALLET_ID_IS_MISSING);
	}

	@Test
	void playerIdIsMissing() {
		wallet.setPlayerId(null);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(wallet));
		assertException(exception, PLAYER_ID_IS_MISSING);
	}

	@Test
	void balanceIsMissing() {
		wallet.setBalance(null);
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(wallet));
		assertException(exception, BALANCE_IS_MISSING);
	}

	@Test
	void playerDoesNotExist() {
		wallet.setPlayerId(UUID.randomUUID().toString());
		ApiException exception = assertThrows(ApiException.class, () -> validator.validate(wallet));
		assertException(exception, PLAYER_DOES_NOT_EXIST);
	}
}