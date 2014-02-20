package com.ss.xpence.model;

import java.util.ArrayList;
import java.util.List;

public class SenderModel {

	private String sender;
	private String selectedBank;
	private List<String> banks;
	private boolean hidden;

	public String getSelectedBank() {
		return selectedBank;
	}

	public void setSelectedBank(String selectedBank) {
		this.selectedBank = selectedBank;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public List<String> getBanks() {
		if (banks == null) {
			banks = new ArrayList<String>();
		}
		return banks;
	}

	public void setBanks(List<String> banks) {
		this.banks = banks;
	}

	public boolean hidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
