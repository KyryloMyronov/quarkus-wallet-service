package com.kyrylomyronov.api.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {

	private String id = null;
	private BigDecimal amount = null;
	private String walletId = null;
	private String transactionType = null;
	private String note;
	private Date modified;

	/**
	 * Id of the transaction. Should be unique.
	 *
	 * @return id
	 **/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Amount to be processed.
	 *
	 * @return amount
	 **/
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Wallet with the Id.
	 *
	 * @return walletId
	 **/
	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	/**
	 * Type of the transaction
	 *
	 * @return transactionType
	 **/
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Short note to the transaction
	 *
	 * @return note
	 **/
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Time of last update
	 *
	 * @return modified
	 */
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
}

