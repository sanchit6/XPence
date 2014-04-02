package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ss.xpence.R;

public class SimpleListItem1Adapter extends ArrayAdapter<String> {

	private final Activity context;

	public SimpleListItem1Adapter(Activity context, List<String> objects) {
		super(context, R.layout.simple_list_item_layout_1, R.id.list_item_1, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String item = getItem(position);

		View view = context.getLayoutInflater().inflate(R.layout.simple_list_item_layout_1, null);

		TextView textView = (TextView) view.findViewById(R.id.list_item_1);
		textView.setText(item);

		return view;
	}

}
