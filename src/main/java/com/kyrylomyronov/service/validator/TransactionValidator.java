package com.kyrylomyronov.service.validator;

import static com.kyrylomyronov.exception.error.TransactionError.AMOUNT_IS_MISSING;
import static com.kyrylomyronov.exception.error.TransactionError.TRANSACTION_ID_IS_MISSING;
import static com.kyrylomyronov.exception.error.TransactionError.TRANSACTION_TYPE_IS_MISSING;
import static com.kyrylomyronov.exception.error.WalletError.WALLET_ID_IS_MISSING;
import static com.kyrylomyronov.service.validator.ValidationUtils.reject;
import static com.kyrylomyronov.service.validator.ValidationUtils.validatePresentAndValid;

import java.math.BigDecimal;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.exception.error.TransactionError;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {

	public void validate(TransactionEntity entity) {
		validateMandatoryFields(entity);
		validateTransactionType(entity);
		validateAmount(entity);
	}

	public void validateTransactionType(TransactionEntity entity) {
		if (entity.getTransactionType() == null) {
			reject(TRANSACTION_TYPE_IS_MISSING);
		}
	}

	private void validateMandatoryFields(TransactionEntity transactionEntity) {
		validatePresentAndValid(transactionEntity.getExternalId(), TRANSACTION_ID_IS_MISSING);
		validatePresentAndValid(transactionEntity.getWallet().getExternalId(), WALLET_ID_IS_MISSING);
	}

	private void validateAmount(TransactionEntity transactionEntity) {
		if (transactionEntity.getAmount() == null) {
			reject(AMOUNT_IS_MISSING);
		}
		if (transactionEntity.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			reject(TransactionError.AMOUNT_IS_NEGATIVE);
		}
	}
}
