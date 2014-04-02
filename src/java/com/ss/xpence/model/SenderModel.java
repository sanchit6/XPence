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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SenderModel other = (SenderModel) obj;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}

}
