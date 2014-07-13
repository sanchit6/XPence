package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ss.xpence.R;

public class DrawerItemAdapter extends ArrayAdapter<String> {

	private final Activity context;

	public DrawerItemAdapter(Activity context, List<String> objects) {
		super(context, R.layout.drawer_item_layout, objects);

		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = context.getLayoutInflater().inflate(R.layout.drawer_item_layout, null);

		TextView tv = (TextView) v.findViewById(R.id.drawer_item_text);
		tv.setText(getItem(position));

		return v;
	}

}
