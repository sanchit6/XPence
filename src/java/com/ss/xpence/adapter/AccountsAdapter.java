package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.xpence.AccountEditor;
import com.ss.xpence.R;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.ConverterUtils;
import com.ss.xpence.view.dialog.DeleteAccountConfirmDialog;

public class AccountsAdapter extends ArrayAdapter<AccountModel> {

	private String[] colors = { "#A20025", "#00ABA9", "#e3c800", "#60A917" };

	private final Activity context;
	private AccountsDAO accountsDAO;

	public AccountsAdapter(Activity context, List<AccountModel> objects, AccountsDAO accountsDAO) {
		super(context, R.layout.account_layout, objects);

		this.context = context;
		this.accountsDAO = accountsDAO;
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

		ImageView accTrashImage = (ImageView) view.findViewById(R.id.acc_trash_button);
		ImageView accEditImage = (ImageView) view.findViewById(R.id.acc_edit_button);

		AccountModel model = getItem(position);

		bankLabel.setText(model.getBankName());
		accountNoLabel.setText(model.getAccountNumber());

		List<String> cards = model.getCardNumbers();
		cardNoLabel.setText(ConverterUtils.accumulate(cards, ","));

		accNameLabel.setText(model.getAccountName());
		accIdLabel.setText(String.valueOf(model.getAccountId()));

		iconBankLabel.setText(model.getBankName().substring(0, 1).toUpperCase());
		iconBankLabel.setBackgroundColor(Color.parseColor(colors[position % 4]));

		accEditImage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				View vx = (View) v.getParent().getParent();

				TextView accNameLabel = (TextView) vx.findViewById(R.id.label_accname);
				TextView bankLabel = (TextView) vx.findViewById(R.id.label_bank);
				TextView accountNoLabel = (TextView) vx.findViewById(R.id.label_account_no);
				TextView cardNoLabel = (TextView) vx.findViewById(R.id.label_card_no);
				TextView accId = (TextView) vx.findViewById(R.id.label_accid);

				Intent intent = new Intent(context, AccountEditor.class);
				intent.putExtra("1", bankLabel.getText().toString());
				intent.putExtra("2", accountNoLabel.getText().toString());
				intent.putExtra("3", cardNoLabel.getText().toString());
				intent.putExtra("4", accNameLabel.getText().toString());
				intent.putExtra("-1", accId.getText().toString());
				context.startActivity(intent);
			}
		});

		accTrashImage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				View vx = (View) v.getParent().getParent();

				TextView accNameLabel = (TextView) vx.findViewById(R.id.label_accname);
				TextView accIdLabel = (TextView) vx.findViewById(R.id.label_accid);

				new DeleteAccountConfirmDialog(accountsDAO, accNameLabel.getText(), accIdLabel.getText()).show(
					context.getFragmentManager(), "1");
			}
		});

		return view;
	}

}
