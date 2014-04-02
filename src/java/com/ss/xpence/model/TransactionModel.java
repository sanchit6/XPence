package com.ss.xpence.model;

import java.util.Date;

public class TransactionModel extends AbstractModel<SMSModel> {
	private Date date;
	private Double amount;
	private String location;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
