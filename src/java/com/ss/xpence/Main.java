package com.ss.xpence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * @param view
	 */
	public void onLoadTransactions(View view) {
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

	/**
	 * @param view
	 */
	public void onLoadSettings(View view) {
		Intent intent = new Intent(getBaseContext(), Settings.class);
		startActivity(intent);
	}
}
