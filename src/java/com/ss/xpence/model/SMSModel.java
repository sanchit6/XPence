package com.ss.xpence.model;

import java.io.Serializable;
import java.util.Date;

public class SMSModel implements Serializable {
	private static final long serialVersionUID = -3894467017818270748L;
	private String message;
	private Date receivedOn;
	private String sender;
	private long id;

	public static SMSModel create(long id) {
		SMSModel m = new SMSModel();
		m.setId(id);
		return m;
	}

	public static SMSModel create(String message) {
		SMSModel m = new SMSModel();
		m.setMessage(message);
		return m;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
