package com.ss.xpence.model;

import com.mongodb.DBObject;

public class ParsersModel {

	private long id;
	private String bank;
	private String containsRegex;
	private String locationRegex;
	private String amountRegex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getContainsRegex() {
		return containsRegex;
	}

	public void setContainsRegex(String containsRegex) {
		this.containsRegex = containsRegex;
	}

	public String getLocationRegex() {
		return locationRegex;
	}

	public void setLocationRegex(String locationRegex) {
		this.locationRegex = locationRegex;
	}

	public String getAmountRegex() {
		return amountRegex;
	}

	public void setAmountRegex(String amountRegex) {
		this.amountRegex = amountRegex;
	}

	public static ParsersModel newModel(DBObject o) {
		ParsersModel model = new ParsersModel();
		model.setBank(o.get("bank").toString());
		model.setAmountRegex(o.get("amount").toString());
		model.setLocationRegex(o.get("location").toString());
		model.setContainsRegex(o.get("contains").toString());
		return model;
	}

}
