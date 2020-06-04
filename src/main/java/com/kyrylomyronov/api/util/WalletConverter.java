package com.kyrylomyronov.api.util;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.kyrylomyronov.api.model.Wallet;
import com.kyrylomyronov.entity.WalletEntity;

/**
 * Utils used to convert from api model to domain entities and vice versa.
 */
public class WalletConverter {

	private WalletConverter() {
		//Utils class that doesn't require an instance.
	}

	public static Wallet toApiObject(WalletEntity entity) {
		if (entity == null) {
			return new Wallet();
		}
		Wallet apiObject = new Wallet();
		apiObject.setId(entity.getExternalId());//Internal entity id will not be exposed
		apiObject.setPlayerId(entity.getPlayerId());
		apiObject.setBalance(entity.getBalance());
		apiObject.setModified(entity.getModified());
		return apiObject;
	}

	public static WalletEntity toEntity(Wallet apiObject) {
		WalletEntity entity = new WalletEntity();
		entity.setBalance(apiObject.getBalance());
		entity.setPlayerId(apiObject.getPlayerId());
		entity.setExternalId(apiObject.getId());
		return entity;
	}

	public static Wallets toApiWallets(List<WalletEntity> walletEntities) {
		Wallets wallets = new Wallets();
		wallets.setList(walletEntities == null || walletEntities.isEmpty() ? emptyList() : walletEntities.stream().map(WalletConverter::toApiObject).collect(toList()));
		return wallets;
	}
}
