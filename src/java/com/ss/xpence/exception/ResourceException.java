package com.ss.xpence.exception;

public class ResourceException extends Exception {
	private static final long serialVersionUID = -377573350978173760L;

	public ResourceException() {
		super();
	}

	public ResourceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ResourceException(String detailMessage) {
		super(detailMessage);
	}

	public ResourceException(Throwable throwable) {
		super(throwable);
	}

}
