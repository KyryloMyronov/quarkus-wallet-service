package com.kyrylomyronov.entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "wallets")
public class WalletEntity extends TrackableEntity {

	@Column(name = "balance")
	private BigDecimal balance;
	@Column(name = "player_id", nullable = false)
	private String playerId;

	@Column(name = "externalId", nullable = false, unique = true)
	private String externalId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransactionEntity> transactions;

	public WalletEntity() {
	}

	public WalletEntity(BigDecimal balance, String playerId) {
		this.balance = balance;
		this.playerId = playerId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String personId) {
		this.playerId = personId;
	}

	public List<TransactionEntity> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionEntity> transactions) {
		this.transactions = transactions;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
