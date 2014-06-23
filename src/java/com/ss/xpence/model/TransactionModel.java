package com.ss.xpence.model;

import java.util.Date;

public class TransactionModel extends AbstractModel<SMSModel> {
	private long id;
	private Date date;
	private Double amount;
	private String location;
	private String comment;

	private AccountModel accountModel;
	private SenderModel senderModel;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AccountModel getAccountModel() {
		return accountModel;
	}

	public void setAccountModel(final AccountModel accountModel) {
		this.accountModel = accountModel;
	}

	public SenderModel getSenderModel() {
		return senderModel;
	}

	public void setSenderModel(final SenderModel senderModel) {
		this.senderModel = senderModel;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

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
