package com.ss.xpence;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Space;

import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.ConverterUtils;
import com.ss.xpence.view.DummyView;

public class AccountEditor extends Activity {

	private boolean isExisting = false;
	private long accountId;

	private AccountsDAO accountsDAO;

	public AccountEditor() throws ResourceException {
		accountsDAO = ResourceManager.get(AccountsDAO.class);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_editor);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			String bank = bundle.getString("1");
			String account = bundle.getString("2");
			String cardNo = bundle.getString("3");
			String accName = bundle.getString("4");

			((EditText) findViewById(R.id.editText_account)).setText(account);
			((EditText) findViewById(R.id.editText_bank)).setText(bank);
			((EditText) findViewById(R.id.editText_accName)).setText(accName);

			if (!cardNo.contains(",")) {
				((EditText) findViewById(R.id.editText_card)).setText(cardNo);
			} else {
				String[] cards = cardNo.split(",");
				((EditText) findViewById(R.id.editText_card)).setText(cards[0]);

				for (int i = 1; i < cards.length; i++) {
					String card = cards[i];

					DummyView v = DummyView.create("text", card, getBaseContext());
					onAddCardRow(v);
				}
			}

			isExisting = true;
			accountId = bundle.getLong("-1");
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
		model.getCardModels().add(
			ConverterUtils.toCardModel((((EditText) findViewById(R.id.editText_card)).getText().toString())));

		GridLayout layout = (GridLayout) findViewById(R.id.dynamic_wrapper_cards);
		int count = layout.getChildCount();
		for (int i = count - 1; i >= 0; --i) {
			View child = layout.getChildAt(i);

			if (child.getId() == R.id.addCardIcon) {
				break;
			}

			if (!(child instanceof EditText)) {
				continue;
			}

			model.getCardModels().add(ConverterUtils.toCardModel((((EditText) child).getText().toString())));
		}

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

		EditText et = new EditText(bet.getContext());
		et.setEms(11);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setSingleLine(true);
		et.setTextColor(Color.BLACK);

		final float scale = getBaseContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (182 * scale + 0.5f);
		et.setWidth(pixels);
		// et.setHint("Enter Card No.");

		if (view instanceof DummyView) {
			et.setText(((DummyView) view).get("text"));
		}

		GridLayout layout = (GridLayout) findViewById(R.id.dynamic_wrapper_cards);
		layout.addView(et);
		layout.addView(new Space(getBaseContext()));
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
