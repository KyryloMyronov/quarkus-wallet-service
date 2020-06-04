package com.kyrylomyronov.entity;

public enum TransactionType {

	CREDIT("credit"),
	DEBIT("debit");

	private String value;

	TransactionType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static TransactionType fromValue(String v) {
		for (TransactionType b : TransactionType.values()) {
			if (String.valueOf(b.value).equalsIgnoreCase(v)) {
				return b;
			}
		}
		return null;
	}
}