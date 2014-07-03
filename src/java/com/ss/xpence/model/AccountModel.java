package com.ss.xpence.model;

import java.util.ArrayList;
import java.util.List;

public class AccountModel {

	private long accountId;
	private String accountName;
	private String bankName;

	private String accountNumber;
	private List<CardModel> cardNumbers;
	private Integer rolloverDate;

	public static AccountModel create(long accountId) {
		AccountModel m = new AccountModel();
		m.setAccountId(accountId);
		return m;
	}

	public Integer getRolloverDate() {
		return rolloverDate;
	}

	public void setRolloverDate(Integer rolloverDate) {
		this.rolloverDate = rolloverDate;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<CardModel> getCardModels() {
		if (cardNumbers == null) {
			cardNumbers = new ArrayList<AccountModel.CardModel>();
		}
		return cardNumbers;
	}

	public List<String> getCardNumbers() {
		if (cardNumbers == null) {
			cardNumbers = new ArrayList<AccountModel.CardModel>();
		}

		List<String> response = new ArrayList<String>();
		for (CardModel model : cardNumbers) {
			response.add(model.getCardNumber());
		}
		return response;
	}

	public void setCardNumbers(List<CardModel> cardNumber) {
		this.cardNumbers = cardNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public static class CardModel {
		private long id;
		private String cardNumber;
		private long accountId;

		public static CardModel create(long accountId) {
			CardModel m = new CardModel();
			m.setAccountId(accountId);
			return m;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getCardNumber() {
			return cardNumber;
		}

		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}

		public long getAccountId() {
			return accountId;
		}

		public void setAccountId(long accountId) {
			this.accountId = accountId;
		}

	}

}
