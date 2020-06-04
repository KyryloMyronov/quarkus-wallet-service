package com.kyrylomyronov.service;

import java.util.List;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.WalletEntity;

public interface TransactionService {

	/**
	 * Returns all transactions for the wallet with provided external ID.
	 * Transactions may be filtered by transaction type.
	 *
	 * @param walletId
	 * @param transactionType
	 * @return
	 */
	List<TransactionEntity> getTransactionsByWalletId(String walletId, String transactionType);

	/**
	 * Returns transaction by it's external ID.
	 *
	 * @param transactionId
	 * @return
	 */
	TransactionEntity getTransaction(String transactionId);

	/**
	 * Method should be used when updating wallet balance only!
	 * Think twice before use it.
	 *
	 * Marked as a deprecated to make sure that developer reads this before use.
	 *
	 * @param transactionEntity
	 * @param wallet
	 * @return
	 */
	@Deprecated
	TransactionEntity createTransaction(TransactionEntity transactionEntity, WalletEntity wallet);

	/**
	 * Get transactions for the Player with provided ID.
	 * Might be filtered by transaction type if filter applied.
	 *
	 * @param type
	 * @param playerId
	 * @return
	 */
	List<TransactionEntity> getTransactionsByPlayerId(String playerId, String type);
}
