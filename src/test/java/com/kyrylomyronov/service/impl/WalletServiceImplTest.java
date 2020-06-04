package com.kyrylomyronov.service.impl;


import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.exception.error.WalletError;
import com.kyrylomyronov.repository.WalletRepository;
import com.kyrylomyronov.service.PlayerApiMock;
import com.kyrylomyronov.service.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static com.kyrylomyronov.exception.error.WalletError.PLAYER_DOES_NOT_EXIST;
import static com.kyrylomyronov.exception.error.WalletError.WALLET_EXISTS;
import static com.kyrylomyronov.service.impl.TestUtils.*;
import static com.kyrylomyronov.service.impl.TransactionServiceImplTest.CREDIT;
import static com.kyrylomyronov.service.impl.TransactionServiceImplTest.DEBIT;
import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class WalletServiceImplTest {
	@Autowired
	private WalletService walletService;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private PlayerApiMock playerApi;

	@AfterEach
	void cleanup() {
		walletRepository.deleteAll();
	}

	@Test
	void createWallet() {
		WalletEntity walletEntity = createNewWallet();
		playerApi.newPlayer(walletEntity.getPlayerId());
		WalletEntity wallet = walletService.createWallet(walletEntity);
		assertWallet(walletEntity, wallet);
	}

	@Test
	void createWalletWithExistingId() {
		WalletEntity walletEntity = createAndSaveWallet();
		WalletEntity wallet = createNewWallet();
		playerApi.newPlayer(wallet.getPlayerId());
		wallet.setExternalId(walletEntity.getExternalId());
		ApiException exception = assertThrows(ApiException.class, () -> walletService.createWallet(wallet));
		assertException(exception, WALLET_EXISTS);
	}

	@Test
	void deleteWallet() {
		WalletEntity wallet = createAndSaveWallet();
		walletService.deleteWallet(wallet.getExternalId());
		assertFalse(walletRepository.getByExternalId(wallet.getExternalId()).isPresent());
	}

	@Test
	void getAllWallets() {
		createAndSaveWallet();
		createAndSaveWallet();
		createAndSaveWallet();

		List<WalletEntity> wallets = walletService.getWallets(null);

		assertEquals(3, wallets.size());

		wallets = walletService.getWallets(" ");

		assertEquals(3, wallets.size());
	}

	@Test
	void getWalletByPlayerId() {
		WalletEntity wallet = createAndSaveWallet();
//		Create wallets with different playerIds
		createAndSaveWallet();
		createAndSaveWallet();
		createAndSaveWallet();

		WalletEntity walletSamePlayer = createNewWallet();
		walletSamePlayer.setPlayerId(wallet.getPlayerId());
		walletService.createWallet(walletSamePlayer);

		List<WalletEntity> result = walletService.getWallets(wallet.getPlayerId());

		assertEquals(2, result.size());
	}


	@Test
	void getWalletWrongPlayerId() {
		WalletEntity wallet = createAndSaveWallet();
//		Create wallets with different playerIds
		createAndSaveWallet();
		createAndSaveWallet();
		createAndSaveWallet();

		WalletEntity walletSamePlayer = createNewWallet();
		walletSamePlayer.setPlayerId(wallet.getPlayerId());
		walletService.createWallet(walletSamePlayer);

		ApiException exception = assertThrows(ApiException.class, () -> walletService.getWallets("wrongId"));

		assertException(exception, PLAYER_DOES_NOT_EXIST);
	}

	@Test
	void getWalletByWalletId() {
		WalletEntity wallet = createAndSaveWallet();
		WalletEntity wallet1 = createAndSaveWallet();
		createAndSaveWallet();
		createAndSaveWallet();

		WalletEntity result = walletService.getWalletByExternalId(wallet.getExternalId());

		assertWallet(wallet, result);

		result = walletService.getWalletByExternalId(wallet1.getExternalId());

		assertWallet(wallet1, result);
	}

	@Test
	void addFundsToWallet() {
		WalletEntity wallet = createAndSaveWallet();
		TransactionEntity credit = createNewTransactionEntity(wallet, CREDIT);
		BigDecimal expectedBalance = wallet.getBalance().add(credit.getAmount());
		WalletEntity result = walletService.updateBalance(wallet.getExternalId(), credit);

		assertEquals(0, expectedBalance.compareTo(result.getBalance()));
	}

	@Test
	void withdrawFundsFromWallet() {
		WalletEntity wallet = createAndSaveWallet();
		TransactionEntity credit = createNewTransactionEntity(wallet, DEBIT);
		BigDecimal expectedBalance = wallet.getBalance().subtract(credit.getAmount());
		WalletEntity result = walletService.updateBalance(wallet.getExternalId(), credit);

		assertEquals(0, expectedBalance.compareTo(result.getBalance()));
	}

	@Test
	void notEnoughFundsForWithdrawal() {
		WalletEntity wallet = createAndSaveWallet();
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		debit.setAmount(wallet.getBalance().add(ONE));
		BigDecimal expectedBalance = wallet.getBalance();

		ApiException exception = assertThrows(ApiException.class, () -> walletService.updateBalance(wallet.getExternalId(), debit));

		assertException(exception, WalletError.NOT_ENOUGH_FUNDS);
		assertEquals(0, expectedBalance.compareTo(wallet.getBalance()));
	}

	private void assertWallet(WalletEntity expected, WalletEntity result) {
		assertAll(
				() -> assertEquals(expected.getExternalId(), result.getExternalId()),
				() -> assertEquals(expected.getPlayerId(), result.getPlayerId()),
				() -> assertEquals(0, expected.getBalance().compareTo(result.getBalance()))
		);
	}

	private WalletEntity createAndSaveWallet() {
		WalletEntity walletEntity = createNewWallet();
		playerApi.newPlayer(walletEntity.getPlayerId());
		return walletRepository.saveAndFlush(walletEntity);
	}
}