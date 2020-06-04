package com.kyrylomyronov.service;

import java.util.List;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.WalletEntity;

public interface WalletService {
	/**
	 * Creates new wallet in the system.
	 *
	 * @param wallet
	 * @return
	 */
	WalletEntity createWallet(WalletEntity wallet);

	/**
	 * Delete the wallet with provided external id and ALL the transactions.
	 *
	 * @param walletId
	 */
	void deleteWallet(String walletId);

	/**
	 * Returns the wallet with provided external ID.
	 *
	 * @param walletExternalId
	 * @return
	 */
	WalletEntity getWalletByExternalId(String walletExternalId);

	/**
	 * Returns all the wallets.
	 * Returns player's wallets if playerId is provided.
	 *
	 * @param playerId
	 * @return
	 */
	List<WalletEntity> getWallets(String playerId);

	/**
	 * Changes wallet's balance with provided transaction.
	 * Debit operation will be declined, if wallet has not enough funds.
	 *
	 * @param walletExternalId
	 * @param transactionEntity
	 * @return
	 */
	WalletEntity updateBalance(String walletExternalId, TransactionEntity transactionEntity);
}
