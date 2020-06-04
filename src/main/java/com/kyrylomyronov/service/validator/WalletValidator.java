package com.kyrylomyronov.service.validator;

import static com.kyrylomyronov.exception.error.WalletError.BALANCE_IS_MISSING;
import static com.kyrylomyronov.exception.error.WalletError.PLAYER_DOES_NOT_EXIST;
import static com.kyrylomyronov.exception.error.WalletError.PLAYER_ID_IS_MISSING;
import static com.kyrylomyronov.exception.error.WalletError.WALLET_ID_IS_MISSING;
import static com.kyrylomyronov.service.validator.ValidationUtils.reject;
import static com.kyrylomyronov.service.validator.ValidationUtils.validatePresentAndValid;

import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.service.PlayerApiMock;
import org.springframework.stereotype.Component;

@Component
public class WalletValidator {

	PlayerApiMock playerApi;

	public WalletValidator(PlayerApiMock playerApi) {
		this.playerApi = playerApi;
	}

	public void validate(WalletEntity wallet) {
		validateMandatoryFields(wallet);
		validatePlayerExists(wallet.getPlayerId());
		validateBalance(wallet);
	}

	public void validatePlayerExists(String playerId) {
		validatePresentAndValid(playerApi.findPlayer(playerId), PLAYER_DOES_NOT_EXIST);
	}

	private void validateBalance(WalletEntity wallet) {
		if (wallet.getBalance() == null) {
			reject(BALANCE_IS_MISSING);
		}
	}

	void validateMandatoryFields(WalletEntity walletEntity) {
		validatePresentAndValid(walletEntity.getExternalId(), WALLET_ID_IS_MISSING);
		validatePresentAndValid(walletEntity.getPlayerId(), PLAYER_ID_IS_MISSING);
	}
}
