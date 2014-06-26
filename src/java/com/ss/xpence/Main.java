package com.ss.xpence;

import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.exception.ResourceException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Load the Parsers from MongoDB at the startup
		try {
			MongoConnector c = ResourceManager.get(MongoConnector.class);
			c.asyncFetchAndCache();
		} catch (ResourceException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.menu_settings:
				Intent intent = new Intent(getBaseContext(), Settings.class);
				startActivity(intent);
				break;
			case R.id.main_log_viewer:
				intent = new Intent(getBaseContext(), LogViewer.class);
				startActivity(intent);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @param view
	 */
	public void onLoadTransactions(View view) {
		MongoConnector c;
		try {
			c = ResourceManager.get(MongoConnector.class);
			if (c.fetchCache() == null) {
				Toast.makeText(getApplicationContext(), "Loading Parsers.. Please Wait!!", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (ResourceException e) {
			throw new RuntimeException(e);
		}

		Intent intent = new Intent(getBaseContext(), TransactionsView.class);
		startActivity(intent);
	}

	/**
	 * @param view
	 */
	public void onLoadAccounts(View view) {
		Intent intent = new Intent(getBaseContext(), AccountsView.class);
		startActivity(intent);
	}

	/**
	 * @param view
	 */
	public void onLoadSenders(View view) {
		Intent intent = new Intent(getBaseContext(), SendersManager.class);
		startActivity(intent);
	}

}
