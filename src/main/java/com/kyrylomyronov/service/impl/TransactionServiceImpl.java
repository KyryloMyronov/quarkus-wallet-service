package com.kyrylomyronov.service.impl;

import static com.kyrylomyronov.exception.error.TransactionError.TRANSACTION_ID_IS_NOT_UNIQUE;
import static com.kyrylomyronov.exception.error.TransactionError.TRANSACTION_NOT_FOUND;
import static com.kyrylomyronov.exception.error.TransactionError.WRONG_TRANSACTION_TYPE;
import static com.kyrylomyronov.exception.error.WalletError.PLAYER_DOES_NOT_EXIST;
import static com.kyrylomyronov.exception.error.WalletError.PLAYER_ID_IS_MISSING;
import static com.kyrylomyronov.exception.error.WalletError.WALLET_NOT_FOUND;
import static com.kyrylomyronov.service.validator.ValidationUtils.reject;

import java.util.List;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.TransactionType;
import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.repository.TransactionRepository;
import com.kyrylomyronov.repository.WalletRepository;
import com.kyrylomyronov.service.PlayerApiMock;
import com.kyrylomyronov.service.TransactionService;
import com.kyrylomyronov.service.validator.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

	private TransactionRepository transactionRepository;
	private TransactionValidator transactionValidator;
	private PlayerApiMock playerApi;

	@Autowired
	private WalletRepository walletRepository;

	public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionValidator transactionValidator, PlayerApiMock playerApi) {
		this.transactionRepository = transactionRepository;
		this.transactionValidator = transactionValidator;
		this.playerApi = playerApi;
	}

	@Override
	public List<TransactionEntity> getTransactionsByWalletId(String walletExternalId, String transactionType) {
		if (transactionType != null && TransactionType.fromValue(transactionType) == null) {
			reject(WRONG_TRANSACTION_TYPE);
		}
		WalletEntity wallet = walletRepository.getByExternalId(walletExternalId).orElseThrow(()-> new ApiException(WALLET_NOT_FOUND));
		return transactionRepository.getAllByWalletAndTransactionType(wallet, TransactionType.fromValue(transactionType));
	}

	@Override
	public TransactionEntity getTransaction(String transactionId) {
		return transactionRepository.getByExternalId(transactionId)
				.orElseThrow(() -> new ApiException(TRANSACTION_NOT_FOUND));
	}

	@Override
	@Transactional(Transactional.TxType.MANDATORY)
	public TransactionEntity createTransaction(TransactionEntity transactionEntity, WalletEntity wallet) {
		transactionEntity.setWallet(wallet);
		transactionValidator.validate(transactionEntity);
		TransactionEntity entity;
		try {
			entity = transactionRepository.saveAndFlush(transactionEntity);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiException(TRANSACTION_ID_IS_NOT_UNIQUE);
		}
		return entity;
	}

	@Override
	public List<TransactionEntity> getTransactionsByPlayerId(String playerId, String type) {
		if (playerId == null) {
			reject(PLAYER_ID_IS_MISSING);
		} else if (playerApi.findPlayer(playerId) == null) {
			reject(PLAYER_DOES_NOT_EXIST);
		}
		if (type != null && TransactionType.fromValue(type) == null) {
			reject(WRONG_TRANSACTION_TYPE);
		}

		return transactionRepository.getAllTransactions(playerId, TransactionType.fromValue(type));
	}
}
