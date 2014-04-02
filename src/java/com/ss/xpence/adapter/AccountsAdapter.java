package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ss.xpence.AccountEditor;
import com.ss.xpence.R;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.view.dialog.DeleteConfirmDialog;

public class AccountsAdapter extends ArrayAdapter<AccountModel> {

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
		ImageView accTrashImage = (ImageView) view.findViewById(R.id.acc_trash_button);
		ImageView accEditImage = (ImageView) view.findViewById(R.id.acc_edit_button);

		AccountModel model = getItem(position);

		bankLabel.setText(model.getBankName());
		accountNoLabel.setText(model.getAccountNumber());
		cardNoLabel.setText(model.getCardNumber());
		accNameLabel.setText(model.getAccountName());
		accIdLabel.setText(String.valueOf(model.getAccountId()));

		accEditImage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LinearLayout view = (LinearLayout) v.getParent();
				LinearLayout view2 = (LinearLayout) ((LinearLayout) v.getParent().getParent())
					.findViewById(R.id.acc_layout_2);

				TextView accNameLabel = (TextView) view.findViewById(R.id.label_accname);
				TextView bankLabel = (TextView) view.findViewById(R.id.label_bank);
				TextView accountNoLabel = (TextView) view2.findViewById(R.id.label_account_no);
				TextView cardNoLabel = (TextView) view2.findViewById(R.id.label_card_no);
				TextView accId = (TextView) view2.findViewById(R.id.label_accid);

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
				LinearLayout view = (LinearLayout) v.getParent();
				LinearLayout view2 = (LinearLayout) ((LinearLayout) v.getParent().getParent())
					.findViewById(R.id.acc_layout_2);

				TextView accNameLabel = (TextView) view.findViewById(R.id.label_accname);
				TextView accIdLabel = (TextView) view2.findViewById(R.id.label_accid);

				new DeleteConfirmDialog(accountsDAO, accNameLabel.getText(), accIdLabel.getText()).show(
					context.getFragmentManager(), "1");
			}
		});

		return view;
	}

}
