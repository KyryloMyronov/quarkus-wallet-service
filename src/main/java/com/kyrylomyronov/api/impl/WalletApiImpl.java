package com.kyrylomyronov.api.impl;

import static com.kyrylomyronov.leovegastest.api.util.TransactionConverter.toApiTransactions;
import static com.kyrylomyronov.leovegastest.api.util.WalletConverter.toApiObject;
import static com.kyrylomyronov.leovegastest.api.util.WalletConverter.toApiWallets;
import static com.kyrylomyronov.leovegastest.api.util.WalletConverter.toEntity;

import com.kyrylomyronov.leovegastest.api.WalletApi;
import com.kyrylomyronov.leovegastest.api.model.Transaction;
import com.kyrylomyronov.leovegastest.api.model.Transactions;
import com.kyrylomyronov.leovegastest.api.model.Wallet;
import com.kyrylomyronov.leovegastest.api.model.Wallets;
import com.kyrylomyronov.leovegastest.api.util.TransactionConverter;
import com.kyrylomyronov.leovegastest.api.util.WalletConverter;
import com.kyrylomyronov.leovegastest.service.TransactionService;
import com.kyrylomyronov.leovegastest.service.WalletService;
import org.springframework.stereotype.Component;

@Component
public class WalletApiImpl implements WalletApi {

	private WalletService walletService;
	private TransactionService transactionService;

	public WalletApiImpl(WalletService walletService, TransactionService transactionService) {
		this.walletService = walletService;
		this.transactionService = transactionService;
	}

	@Override
	public Wallet updateBalance(String externalId, Transaction newCreditTransaction) {
		return toApiObject(walletService.updateBalance(externalId, TransactionConverter.toEntity(newCreditTransaction)));
	}

	@Override
	public Wallet createWallet(Wallet newWallet) {
		return toApiObject(walletService.createWallet(toEntity(newWallet)));
	}

	@Override
	public void deleteWallet(String externalId) {
		walletService.deleteWallet(externalId);
	}

	@Override
	public Transaction getTransaction(String transactionId) {
		return TransactionConverter.toApiObject(transactionService.getTransaction(transactionId));
	}

	@Override
	public Transactions getWalletTransactions(String walletExternalId, String type) {
		return toApiTransactions(transactionService.getTransactionsByWalletId(walletExternalId, type));
	}

	@Override
	public Transactions getPlayerTransactions(String playerId, String type) {
		return toApiTransactions(transactionService.getTransactionsByPlayerId(playerId, type));
	}

	@Override
	public Wallet getWallet(String externalId) {
		return WalletConverter.toApiObject(walletService.getWalletByExternalId(externalId));
	}

	@Override
	public Wallets getWallets(String playerId) {
		return toApiWallets(walletService.getWallets(playerId));
	}
}
