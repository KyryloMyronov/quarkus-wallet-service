package com.kyrylomyronov.service.impl;

import static com.kyrylomyronov.exception.error.TransactionError.WRONG_TRANSACTION_TYPE;
import static com.kyrylomyronov.exception.error.WalletError.NOT_ENOUGH_FUNDS;
import static com.kyrylomyronov.exception.error.WalletError.WALLET_NOT_FOUND;
import static java.util.UUID.randomUUID;

import java.math.BigDecimal;
import java.util.List;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.TransactionType;
import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.exception.error.WalletError;
import com.kyrylomyronov.repository.WalletRepository;
import com.kyrylomyronov.service.TransactionService;
import com.kyrylomyronov.service.WalletService;
import com.kyrylomyronov.service.validator.WalletValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

	private WalletRepository walletRepository;
	private WalletValidator walletValidator;
	private TransactionService transactionService;

	public WalletServiceImpl(WalletRepository walletRepository, WalletValidator walletValidator, TransactionService transactionService) {
		this.walletRepository = walletRepository;
		this.walletValidator = walletValidator;
		this.transactionService = transactionService;
	}

	@Override
	public WalletEntity createWallet(WalletEntity wallet) {
		if (wallet.getExternalId() == null || wallet.getExternalId().trim().isEmpty()) {
			wallet.setExternalId(randomUUID().toString());
		}
		walletValidator.validate(wallet);
		try {
			wallet = walletRepository.saveAndFlush(wallet);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiException(WalletError.WALLET_EXISTS);
		}
		return wallet;
	}

	@Transactional
	@Override
	public void deleteWallet(String walletExternalId) {
		if (walletRepository.deleteByExternalId(walletExternalId) == 0) {
			throw new ApiException(WALLET_NOT_FOUND);
		}
	}

	@Transactional
	@Override
	public WalletEntity getWalletByExternalId(String walletExternalId) {
		return walletRepository.getByExternalId(walletExternalId).orElseThrow(() -> new ApiException(WALLET_NOT_FOUND));
	}

	@Transactional
	@Override
	public List<WalletEntity> getWallets(String playerId) {
		if (playerId == null || playerId.trim().isEmpty()) {
			return walletRepository.findAll();
		} else {
			walletValidator.validatePlayerExists(playerId);
			return walletRepository.getByPlayerId(playerId);
		}
	}

	@Transactional
	@Override
	public WalletEntity updateBalance(String walletExternalId, TransactionEntity transactionEntity) {
		if (transactionEntity.getTransactionType() == null) {
			throw new ApiException(WRONG_TRANSACTION_TYPE);
		}
		return transactionEntity.getTransactionType() == TransactionType.DEBIT ? debitBalance(walletExternalId, transactionEntity) : creditBalance(walletExternalId, transactionEntity);
	}

	@Transactional
	public WalletEntity creditBalance(String walletExternalId, TransactionEntity transactionEntity) {
		WalletEntity wallet = getWalletByExternalId(walletExternalId);
		BigDecimal currentBalance = wallet.getBalance();
		wallet.setBalance(currentBalance.add(transactionEntity.getAmount().abs()));
		transactionService.createTransaction(transactionEntity, wallet);
		return walletRepository.saveAndFlush(wallet);
	}

	@Transactional
	public WalletEntity debitBalance(String walletExternalId, TransactionEntity transactionEntity) {
		WalletEntity wallet = getWalletByExternalId(walletExternalId);
		BigDecimal currentBalance = wallet.getBalance();
		if (isEnoughFundsForDebit(wallet, transactionEntity)) {
			wallet.setBalance(currentBalance.subtract(transactionEntity.getAmount().abs()));
		} else {
			throw new ApiException(NOT_ENOUGH_FUNDS);
		}
		transactionService.createTransaction(transactionEntity, wallet);
		return walletRepository.saveAndFlush(wallet);
	}

	private boolean isEnoughFundsForDebit(WalletEntity wallet, TransactionEntity transactionEntity) {
		return wallet.getBalance().compareTo(transactionEntity.getAmount().abs()) >= 0;
	}
}
