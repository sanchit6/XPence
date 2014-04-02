package com.ss.xpence;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.ss.xpence.adapter.SenderAdapter;
import com.ss.xpence.content.handler.MessagesHandler;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.dataaccess.SendersDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.SenderModel;

public class SendersManager extends Activity {

	private AccountsDAO accountsDB = new AccountsDAO();
	private SendersDAO sendersDAO = new SendersDAO();

	private SenderAdapter adapter;
	private List<SenderModel> objects;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_senders_manager);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ListView view = (ListView) findViewById(R.id.senders_manager);

		List<String> banks = new ArrayList<String>();
		banks.add("-");
		for (AccountModel item : accountsDB.queryAll(this)) {
			if (!banks.contains(item.getBankName())) {
				banks.add(item.getBankName());
			}
		}

		List<SenderModel> dbList = sendersDAO.queryAll(getBaseContext());
		if (dbList != null) {
			for (SenderModel sender : dbList) {
				sender.setBanks(banks);
			}
		}

		List<SenderModel> msgList = new ArrayList<SenderModel>();
		for (String sender : MessagesHandler.fetchUniqueSenders(this)) {
			if (sender.startsWith("+")) {
				continue;
			}

			SenderModel model = new SenderModel();
			model.setSender(sender);
			model.setBanks(banks);
			msgList.add(model);
		}

		objects = merge(dbList, msgList);

		adapter = new SenderAdapter(this, objects);
		view.setAdapter(adapter);
	}

	private List<SenderModel> merge(List<SenderModel> dbList, List<SenderModel> msgList) {
		List<SenderModel> mergedList = new ArrayList<SenderModel>(dbList);

		for (SenderModel item : msgList) {
			if (!mergedList.contains(item)) {
				mergedList.add(item);
			}
		}

		return mergedList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_senders_manager, menu);
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
	public void onShowHiddenClicked(View context) {
		if (!(context instanceof CheckBox)) {
			return;
		}

		CheckBox checkBox = (CheckBox) context;
		boolean showHidden = checkBox.isChecked();
		adapter.setShowHidden(showHidden);

		adapter.resetAdapter();
	}

	/**
	 * @param context
	 */
	public void onPersistData(View context) {
		sendersDAO.clear(getBaseContext());

		for (SenderModel model : objects) {
			sendersDAO.insert(getBaseContext(), model);
		}
	}

}
