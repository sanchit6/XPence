package com.ss.xpence.model;

import java.util.Date;

public class TransactionModel implements BaseModel, Comparable<TransactionModel> {
	private Date date;
	private AccountModel account;
	private String cardNumber;

	private Double amount;
	private String location;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AccountModel getAccount() {
		return account;
	}

	public void setAccount(AccountModel account) {
		this.account = account;
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

	public int compareTo(TransactionModel another) {
		return date.equals(another.getDate()) ? 0 : date.after(another.getDate()) ? -1 : 1;
	}

}
