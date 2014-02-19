package com.ss.xpence;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ss.xpence.adapter.AccountsAdapter;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.model.AccountModel;

public class AccountsView extends Activity {

	private AccountsDAO accountsDAO;

	public AccountsView() {
		accountsDAO = new AccountsDAO();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		List<AccountModel> objects = accountsDAO.queryAll(getBaseContext());

		AccountsAdapter adapter = new AccountsAdapter(this, objects);

		ListView listView = (ListView) findViewById(R.id.accounts_listing);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_accounts_view, menu);
		return true;
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

	/**
	 * @param context
	 */
	public void onAddAccount(View context) {
		Intent intent = new Intent(getBaseContext(), AccountEditor.class);
		startActivity(intent);
	}

	/**
	 * @param context
	 */
	public void onClearAllAccounts(View context) {
		accountsDAO.clear(getBaseContext());

		List<AccountModel> objects = accountsDAO.queryAll(getBaseContext());

		AccountsAdapter adapter = new AccountsAdapter(this, objects);

		ListView listView = (ListView) findViewById(R.id.accounts_listing);
		listView.setAdapter(adapter);
	}

}
