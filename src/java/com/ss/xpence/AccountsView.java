package com.ss.xpence;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ss.xpence.adapter.AccountsAdapter;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.model.AccountModel;

public class AccountsView extends Activity {

	List<AccountModel> objects = null;
	AccountsAdapter adapter = null;

	private AccountsDAO accountsDAO;

	public AccountsView() {
		accountsDAO = new AccountsDAO();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		objects = accountsDAO.queryAll(getBaseContext());

		adapter = new AccountsAdapter(this, objects, accountsDAO);

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
			case R.id.accounts_view_add_new:
				onAddAccount();
				break;
			case R.id.accounts_view_delete_all:

				new AlertDialog.Builder(this).setTitle("CONFIRM").setMessage("Delete All Accounts??")
					.setPositiveButton("Yes", new OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							onClearAllAccounts();
						}
					}).setNegativeButton("Cancel", null).show();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onAddAccount() {
		Intent intent = new Intent(getBaseContext(), AccountEditor.class);
		startActivity(intent);
	}

	private void onClearAllAccounts() {
		accountsDAO.clear(getBaseContext());

		if (objects != null) {
			objects.clear();
			adapter.notifyDataSetChanged();
		}
	}

	public void refreshList() {
		List<AccountModel> objects = accountsDAO.queryAll(getBaseContext());

		AccountsAdapter adapter = new AccountsAdapter(this, objects, accountsDAO);

		ListView listView = (ListView) findViewById(R.id.accounts_listing);
		listView.setAdapter(adapter);
	}

}
