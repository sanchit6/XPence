package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ss.xpence.R;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.ConverterUtils;

public class AccountsAdapter extends ArrayAdapter<AccountModel> {

	private String[] colors = { "#99b433", "#7e3878", "#1d1d1d", "#00aba9", "#e3a21a", "#da532c" };

	private final Activity context;

	public AccountsAdapter(Activity context, List<AccountModel> objects) {
		super(context, R.layout.account_layout, objects);

		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Generate the view from layout
		View view = context.getLayoutInflater().inflate(R.layout.account_layout, null);

		TextView bankLabel = (TextView) view.findViewById(R.id.label_bank);
		TextView accountNoLabel = (TextView) view.findViewById(R.id.label_account_no);
		TextView cardNoLabel = (TextView) view.findViewById(R.id.label_card_no);
		TextView accNameLabel = (TextView) view.findViewById(R.id.label_accname);
		TextView accIdLabel = (TextView) view.findViewById(R.id.label_accid);
		TextView iconBankLabel = (TextView) view.findViewById(R.id.icon_bank);

		AccountModel model = getItem(position);

		bankLabel.setText(model.getBankName());
		accountNoLabel.setText(model.getAccountNumber());

		List<String> cards = model.getCardNumbers();
		cardNoLabel.setText(ConverterUtils.accumulate(cards, ","));

		accNameLabel.setText(model.getAccountName());
		accIdLabel.setText(String.valueOf(model.getAccountId()));

		iconBankLabel.setText(model.getBankName() != null && model.getBankName().length() > 0 ? model.getBankName()
			.substring(0, 1).toUpperCase() : null);
		iconBankLabel.setBackgroundColor(Color.parseColor(colors[position % colors.length]));

		return view;
	}

}
