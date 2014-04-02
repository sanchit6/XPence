package com.ss.xpence;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.model.AccountModel;

public class AccountEditor extends Activity {

	private boolean isExisting = false;
	private long accountId;

	private AccountsDAO accountsDAO = new AccountsDAO();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_editor);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			String bank = bundle.getString("1");
			String account = bundle.getString("2");
			String card = bundle.getString("3");
			String accName = bundle.getString("4");

			((EditText) findViewById(R.id.editText_account)).setText(account);
			((EditText) findViewById(R.id.editText_bank)).setText(bank);
			((EditText) findViewById(R.id.editText_card)).setText(card);
			((EditText) findViewById(R.id.editText_accName)).setText(accName);

			isExisting = true;
			accountId = Long.valueOf(bundle.getString("-1"));
		} else {
			isExisting = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_account_editor, menu);
		return true;
	}

	/**
	 * @param view
	 */
	public void onFormSubmitted(View view) {
		AccountModel model = new AccountModel();

		model.setAccountName(((EditText) findViewById(R.id.editText_accName)).getText().toString());
		model.setAccountNumber(((EditText) findViewById(R.id.editText_account)).getText().toString());
		model.setBankName(((EditText) findViewById(R.id.editText_bank)).getText().toString());
		model.setCardNumber(((EditText) findViewById(R.id.editText_card)).getText().toString());

		if (isExisting) {
			accountsDAO.delete(getBaseContext(), String.valueOf(accountId));
		}

		long id = accountsDAO.insert(getBaseContext(), model);
		model.setAccountId(id);

		NavUtils.navigateUpFromSameTask(this);
	}

	/**
	 * @param view
	 */
	public void onAddCardRow(View view) {
		EditText bet = (EditText) findViewById(R.id.editText_card);

		EditText et = new EditText(getApplicationContext());
		MarginLayoutParams marginLayoutParams = new MarginLayoutParams(182, bet.getLayoutParams().height);
		marginLayoutParams.setMargins(0, 5, 0, 10);
		et.setLayoutParams(marginLayoutParams);
		et.setEms(13);
		et.setInputType(bet.getInputType());
		et.setSingleLine(true);
		et.setBackgroundColor(Color.WHITE);
		et.setTextColor(Color.BLACK);

		LinearLayout layout = (LinearLayout) findViewById(R.id.dynamic_wrapper_cards);
		layout.addView(et);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
