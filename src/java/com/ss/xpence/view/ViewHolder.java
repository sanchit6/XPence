package com.ss.xpence.view;

import android.view.View;

public class ViewHolder<T extends View> {

	private T view;

	public T get() {
		return view;
	}

	public void set(T view) {
		this.view = view;
	}

}
