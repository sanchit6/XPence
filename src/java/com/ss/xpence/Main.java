package com.ss.xpence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.dataaccess.ParsersDAO;
import com.ss.xpence.exception.ResourceException;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Load the Parsers from MongoDB at the startup if needed
		try {
			int countInDb = ResourceManager.get(ParsersDAO.class).queryAll(this).size();

			if (countInDb == 0) {
				ResourceManager.get(MongoConnector.class).doFetchInBackground(this);
			}
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
			case R.id.main_reload_parsers:
				try {
					ResourceManager.get(ParsersDAO.class).delete(this);
					ResourceManager.get(MongoConnector.class).doFetchInBackground(this);
				} catch (ResourceException e) {
					throw new RuntimeException(e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
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
			if (c.isFetchInProgress()) {
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
