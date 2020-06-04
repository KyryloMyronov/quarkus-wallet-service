package com.kyrylomyronov.repository;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.kyrylomyronov.entity.TransactionType;

/**
 * Stores transaction type as it's value but not as an integer in the DB.
 *
 */
@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {
	@Override
	public String convertToDatabaseColumn(TransactionType attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.value();
	}

	@Override
	public TransactionType convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return TransactionType.fromValue(dbData);
	}
}
