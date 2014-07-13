package com.ss.xpence.adapter;

import java.util.List;

import com.ss.xpence.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogAdapter extends ArrayAdapter<String> {

	private final Activity context;

	public LogAdapter(Activity context, List<String> objects) {
		super(context, R.layout.account_layout, objects);

		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.HORIZONTAL);

		String model = getItem(position);

		TextView v1 = new TextView(context);
		v1.setText(String.valueOf(position));

		TextView v2 = new TextView(context);
		v2.setText(model);

		l.addView(v1);
		l.addView(v2);

		return l;
	}

}
