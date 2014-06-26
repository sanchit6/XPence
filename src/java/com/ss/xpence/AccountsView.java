package com.ss.xpence;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.ss.xpence.adapter.AccountsAdapter;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.ConverterUtils;
import com.ss.xpence.view.dialog.DeleteAccountConfirmDialog;

public class AccountsView extends Activity {

	List<AccountModel> objects = null;
	AccountsAdapter adapter = null;

	private AccountsDAO accountsDAO;

	public AccountsView() throws ResourceException {
		accountsDAO = ResourceManager.get(AccountsDAO.class);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		objects = accountsDAO.queryAll(getBaseContext());

		adapter = new AccountsAdapter(this, objects);

		ListView listView = (ListView) findViewById(R.id.accounts_listing);
		listView.setAdapter(adapter);

		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_accounts_view, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.accounts_view_context_view, menu);
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.accounts_context_menu_edit:
				AccountModel o = objects.get(Long.valueOf(info.id).intValue());
				Intent intent = new Intent(this, AccountEditor.class);
				intent.putExtra("1", o.getBankName());
				intent.putExtra("2", o.getAccountNumber());
				intent.putExtra("3", ConverterUtils.accumulate(o.getCardNumbers(), ","));
				intent.putExtra("4", o.getAccountName());
				intent.putExtra("-1", o.getAccountId());
				startActivity(intent);
				return true;
			case R.id.accounts_context_menu_delete:
				o = objects.get(Long.valueOf(info.id).intValue());
				new DeleteAccountConfirmDialog(accountsDAO, o.getAccountName(), o.getAccountId()).show(
					getFragmentManager(), "1");
				return true;
			default:
				return super.onContextItemSelected(item);
		}
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
		adapter.clear();
		adapter.addAll(accountsDAO.queryAll(getBaseContext()));
		adapter.notifyDataSetChanged();
	}

}
