package com.ss.xpence.card.thisweek;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ss.xpence.R;

public class ThisWeekCardAdapter extends ArrayAdapter<ThisWeekModel> {

	private final Activity context;

	public ThisWeekCardAdapter(Activity context, List<ThisWeekModel> objects) {
		super(context, R.layout.card_this_week_layout, objects);

		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// Generate the view from layout
			convertView = context.getLayoutInflater().inflate(R.layout.card_this_week_layout, null);
		}

		TextView accountLabel = (TextView) convertView.findViewById(R.id.card_this_week_account);
		TextView amountLabel = (TextView) convertView.findViewById(R.id.card_this_week_amount);

		ThisWeekModel model = getItem(position);

		accountLabel.setText(model.getAccountName());
		amountLabel.setText(String.valueOf(model.getAmount()));

		return convertView;
	}

}
