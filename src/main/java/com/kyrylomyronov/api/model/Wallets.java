package com.kyrylomyronov.api.model;

import java.util.ArrayList;
import java.util.List;

public class Wallets {

	private List<Wallet> list = new ArrayList<>();

	/**
	 * Get wallets list
	 *
	 * @return list
	 **/
	public List<Wallet> getList() {
		return list;
	}

	public void setList(List<Wallet> list) {
		this.list = list;
	}

}
