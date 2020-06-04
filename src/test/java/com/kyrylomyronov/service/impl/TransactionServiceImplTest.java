package com.kyrylomyronov.service.impl;


import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.WalletEntity;
import com.kyrylomyronov.exception.ApiException;
import com.kyrylomyronov.exception.error.TransactionError;
import com.kyrylomyronov.repository.TransactionRepository;
import com.kyrylomyronov.repository.WalletRepository;
import com.kyrylomyronov.service.PlayerApiMock;
import com.kyrylomyronov.service.TransactionService;
import com.kyrylomyronov.service.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kyrylomyronov.exception.error.TransactionError.TRANSACTION_ID_IS_NOT_UNIQUE;
import static com.kyrylomyronov.service.impl.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class TransactionServiceImplTest {

	@Autowired
	private WalletService walletService;
	@Autowired
	private PlayerApiMock playerApi;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private WalletRepository walletRepository;

	public static final String DEBIT = "debit";
	public static final String CREDIT = "credit";

	private WalletEntity wallet;

	@BeforeEach
	void setup() {
		WalletEntity walletEntity = createNewWallet();
		playerApi.newPlayer(walletEntity.getPlayerId());
		wallet = walletService.createWallet(walletEntity);
	}

	@AfterEach
	void cleanup() {
		walletRepository.deleteAll();
	}

	@Test
	@Transactional
	void getTransactionByTransactionId() {
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit);

		TransactionEntity credit = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit);

		TransactionEntity result = transactionService.getTransaction(debit.getExternalId());
		assertTransaction(debit, result);
		result = transactionService.getTransaction(credit.getExternalId());
		assertTransaction(credit, result);
	}


	@Test
	void getAllTransactionsByWalletId() {
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit);
		TransactionEntity debit2 = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit2);

		TransactionEntity credit = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit);
		TransactionEntity credit2 = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit2);

		List<TransactionEntity> noFilter = transactionService.getTransactionsByWalletId(wallet.getExternalId(), null);

		assertEquals(4, noFilter.size());
	}

	@Test
	void getAllTransactionsByWalletIdAndTransactionTypeFilter() {
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit);
		TransactionEntity debit2 = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit2);

		TransactionEntity credit = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit);
		TransactionEntity credit2 = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit2);

		//Applying transaction type filter
		List<TransactionEntity> debits = transactionService.getTransactionsByWalletId(wallet.getExternalId(), DEBIT);
		List<TransactionEntity> credits = transactionService.getTransactionsByWalletId(wallet.getExternalId(), DEBIT);

		assertAll(
				() -> assertEquals(2, debits.size()),
				() -> assertEquals(2, credits.size())
		);
	}

	@Test
	void getAllTransactionsByWalletIdAndWrongTransactionTypeFilter() {
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit);
		TransactionEntity debit2 = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit2);

		TransactionEntity credit = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit);

		TransactionEntity credit2 = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), credit2);

		//Applying wrong transaction type filter
		ApiException wrongTypeException = assertThrows(ApiException.class, () -> transactionService.getTransactionsByWalletId(wallet.getExternalId(), "dibit"));
		ApiException emptyTypeException = assertThrows(ApiException.class, () -> transactionService.getTransactionsByWalletId(wallet.getExternalId(), "  "));

		assertException(wrongTypeException, TransactionError.WRONG_TRANSACTION_TYPE);
		assertException(emptyTypeException, TransactionError.WRONG_TRANSACTION_TYPE);
	}


	@Test
	@Transactional
	void getTransactionsByPLayerId() {
		TransactionEntity transaction = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), transaction);

		WalletEntity wallet2 = createNewWallet();
		playerApi.newPlayer(wallet2.getPlayerId());
		wallet2 = walletService.createWallet(wallet2);

		TransactionEntity transaction2 = createNewTransactionEntity(wallet2, DEBIT);
		walletService.updateBalance(wallet2.getExternalId(), transaction2);

		List<TransactionEntity> result = transactionService.getTransactionsByPlayerId(wallet.getPlayerId(), null);

		assertEquals(1, result.size());
		assertTransaction(transaction, result.get(0));

		result = transactionService.getTransactionsByPlayerId(wallet2.getPlayerId(), null);

		assertEquals(1, result.size());
		assertTransaction(transaction2, result.get(0));
	}

	@Test
	@Transactional
	void getTransactionsByPLayerIdAndTransactionTypeFilter() {
		TransactionEntity debit1Pl1 = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit1Pl1);
		TransactionEntity debit2Pl1 = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit2Pl1);
		TransactionEntity creditPl1 = createNewTransactionEntity(wallet, CREDIT);
		walletService.updateBalance(wallet.getExternalId(), creditPl1);

		WalletEntity wallet2 = createNewWallet();
		playerApi.newPlayer(wallet2.getPlayerId());
		wallet2 = walletService.createWallet(wallet2);

		TransactionEntity debitPl2 = createNewTransactionEntity(wallet2, DEBIT);
		walletService.updateBalance(wallet2.getExternalId(), debitPl2);
		TransactionEntity creditPl2 = createNewTransactionEntity(wallet2, CREDIT);
		walletService.updateBalance(wallet2.getExternalId(), creditPl2);

		List<TransactionEntity> result = transactionService.getTransactionsByPlayerId(wallet.getPlayerId(), DEBIT);

		assertEquals(2, result.size());

		result = transactionService.getTransactionsByPlayerId(wallet.getPlayerId(), CREDIT);
		assertEquals(1, result.size());
		assertTransaction(creditPl1, result.get(0));

		result = transactionService.getTransactionsByPlayerId(wallet2.getPlayerId(), DEBIT);
		assertEquals(1, result.size());
		assertTransaction(debitPl2, result.get(0));
	}

	@Test
	void cantCreateTransactionEntityWithoutDbTransaction() {
		assertThrows(IllegalTransactionStateException.class, () -> transactionService.createTransaction(createNewTransactionEntity(wallet, CREDIT), wallet));
	}

	@Test
	void deleteTransactionsWhenWalletDeleted() {
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit);
		TransactionEntity debit2 = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit2);
		List<TransactionEntity> allTransactions = transactionService.getTransactionsByWalletId(wallet.getExternalId(), null);

		assertEquals(2, allTransactions.size());

		walletService.deleteWallet(wallet.getExternalId());

		allTransactions = transactionRepository.getAllByWalletIdAndTransactionType(wallet.getExternalId(), null);
//		allTransactions = transactionService.getTransactionsByWalletId(wallet.getExternalId(), null);

		assertTrue(allTransactions.isEmpty());
	}

	@Test
	void createTransactionNotUniqueId() {
		TransactionEntity debit = createNewTransactionEntity(wallet, DEBIT);
		walletService.updateBalance(wallet.getExternalId(), debit);

		TransactionEntity debit2 = createNewTransactionEntity(wallet, DEBIT);
		debit2.setExternalId(debit.getExternalId());
		ApiException exception = assertThrows(ApiException.class, () -> walletService.updateBalance(wallet.getExternalId(), debit2));
		assertException(exception, TRANSACTION_ID_IS_NOT_UNIQUE);
	}


	private void assertTransaction(TransactionEntity expected, TransactionEntity result) {
		assertAll(
				() -> assertEquals(0, expected.getAmount().compareTo(result.getAmount())),
				() -> assertEquals(expected.getTransactionType(), result.getTransactionType()),
				() -> assertEquals(expected.getExternalId(), result.getExternalId()),
				() -> assertEquals(expected.getWallet().getExternalId(), result.getWallet().getExternalId()),
				() -> assertEquals(expected.getNote(), result.getNote())
		);
	}

}