package com.ss.xpence.model;

import java.io.Serializable;
import java.util.Date;

public class SMSModel implements Serializable {
	private static final long serialVersionUID = -3894467017818270748L;
	private String message;
	private Date receivedOn;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getReceivedOn() {
		return (Date) receivedOn.clone();
	}

	public void setReceivedOn(Date receivedOn) {
		this.receivedOn = (Date) receivedOn.clone();
	}

}
