package com.kyrylomyronov.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "transactions")
public class TransactionEntity extends TrackableEntity {

	@Column(name = "external_id", nullable = false, unique = true)
	private String externalId;

	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;

	@Column(name = "amount", nullable = false)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal amount;

	@Column(name = "note")
	private String note;

	@JsonIgnoreProperties("transactions")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id")
	private WalletEntity wallet;


	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public WalletEntity getWallet() {
		return wallet;
	}

	public void setWallet(WalletEntity wallet) {
		this.wallet = wallet;
	}

}
