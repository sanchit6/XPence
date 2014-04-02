package com.ss.xpence.model;

public abstract class AbstractModel<T> {

	private T parentModel;

	public void setParentModel(T parentModel) {
		this.parentModel = parentModel;
	}

	public T getParentModel() {
		return parentModel;
	}

}
