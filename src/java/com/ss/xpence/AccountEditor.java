package com.ss.xpence;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.Constants.CardType;

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
		model.setCardType(CardType.DEBIT_CARD);

		if (isExisting) {
			accountsDAO.delete(getBaseContext(), String.valueOf(accountId));
		}

		long id = accountsDAO.insert(getBaseContext(), model);
		model.setAccountId(id);

		NavUtils.navigateUpFromSameTask(this);
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
