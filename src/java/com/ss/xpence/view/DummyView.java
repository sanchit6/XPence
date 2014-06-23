package com.ss.xpence.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;

public class DummyView extends View {

	private Map<String, String> vals = new HashMap<String, String>();

	public DummyView(Context context) {
		super(context);
	}

	public String get(String key) {
		return vals.get(key);
	}

	public void put(String key, String val) {
		vals.put(key, val);
	}

	public boolean exists(String key) {
		return vals.containsKey(key);
	}

	public static DummyView create(String key, String val, Context context) {
		DummyView v = new DummyView(context);
		v.put(key, val);
		return v;
	}

}
