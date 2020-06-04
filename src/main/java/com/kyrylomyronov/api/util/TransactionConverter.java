package com.kyrylomyronov.api.util;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.kyrylomyronov.api.model.Transaction;
import com.kyrylomyronov.leovegastest.entity.TransactionEntity;
import com.kyrylomyronov.leovegastest.entity.TransactionType;

/**
 * Utils used to convert from api model to domain entities and vice versa.
 */
public class TransactionConverter {

	private TransactionConverter() {
		//Utils class that doesn't require an instance.
	}

	public static Transaction toApiObject(TransactionEntity entity) {
		Transaction apiObject = new Transaction();
		apiObject.setAmount(entity.getAmount());
		apiObject.setId(entity.getExternalId());
		apiObject.setWalletId(entity.getWallet().getExternalId());
		apiObject.setNote(entity.getNote());
		apiObject.setTransactionType(entity.getTransactionType().toString());
		apiObject.setModified(entity.getModified());
		return apiObject;
	}

	public static TransactionEntity toEntity(Transaction apiObject) {
		TransactionEntity entity = new TransactionEntity();
		entity.setExternalId(apiObject.getId());
		entity.setAmount(apiObject.getAmount());
		entity.setTransactionType(TransactionType.fromValue(apiObject.getTransactionType()));
		entity.setNote(apiObject.getNote());
		return entity;
	}

	public static Transactions toApiTransactions(List<TransactionEntity> entities) {
		Transactions transactions = new Transactions();
		transactions.setList(entities.stream().map(TransactionConverter::toApiObject).collect(toList()));
		return transactions;
	}
}
