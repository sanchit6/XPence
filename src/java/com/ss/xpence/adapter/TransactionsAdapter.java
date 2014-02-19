package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ss.xpence.R;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.util.ConverterUtils;

public class TransactionsAdapter extends ArrayAdapter<TransactionModel> {

	private static final String STATEMENT = "STATEMENT";

	private final Activity context;

	public TransactionsAdapter(Activity context, List<TransactionModel> list) {
		super(context, R.layout.transaction_layout, list);
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Generate the view from layout
		View view = context.getLayoutInflater().inflate(R.layout.transaction_layout, null);

		LinearLayout layout = (LinearLayout) view.findViewById(R.id.transaction_row);
		if (position % 2 == 0) {
			layout.setBackgroundColor(getContext().getResources().getColor(R.color.LightCyan));
		}

		TextView dateLabel = (TextView) view.findViewById(R.id.label_tran_notf_date);
		TextView amountLabel = (TextView) view.findViewById(R.id.label_tran_notf_amount);
		TextView locLabel = (TextView) view.findViewById(R.id.label_tran_notf_loc);

		TransactionModel transactionModel = getItem(position);

		String dateValue = ConverterUtils.safeToString(ConverterUtils.formatDate(transactionModel.getDate()));
		String amountValue = ConverterUtils.safeToString(transactionModel.getAmount());
		String locValue = ConverterUtils.safeToString(transactionModel.getLocation());

		if (locLabel == null || !STATEMENT.equals(locValue)) {
			dateLabel.setText(dateValue);
		} else {
			dateLabel.setText("");
		}
		amountLabel.setText(amountValue);

		if (locLabel != null) {
			locLabel.setText(locValue);

			if (STATEMENT.equals(locValue)) {
				((LinearLayout) view).setBackgroundColor(Color.DKGRAY);
				locLabel.setTextColor(Color.WHITE);
				amountLabel.setTextColor(Color.WHITE);
			}
		}

		return view;
	}

}
