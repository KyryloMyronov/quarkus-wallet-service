package com.kyrylomyronov.api.model;

import java.math.BigDecimal;
import java.util.Date;

public class Wallet {
	private String id = null;
	private String playerId = null;
	private BigDecimal balance = null;
	private Date modified;

	/**
	 * External ID of the wallet
	 *
	 * @return externalId
	 **/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Id of the player that owns of the wallet.
	 *
	 * @return personId
	 **/
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * Amount of funds available on account
	 *
	 * @return balance
	 **/
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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
