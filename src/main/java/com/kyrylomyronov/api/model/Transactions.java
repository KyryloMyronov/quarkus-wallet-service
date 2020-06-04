package com.kyrylomyronov.api.model;

import java.util.ArrayList;
import java.util.List;

public class Transactions {

	private List<Transaction> list = new ArrayList<>();

	/**
	 * Get transactions list
	 *
	 * @return list
	 **/
	public List<Transaction> getList() {
		return list;
	}

	public void setList(List<Transaction> list) {
		this.list = list;
	}
}
