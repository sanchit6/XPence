package com.ss.xpence;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.ss.xpence.adapter.MainScreenListingAdapter;
import com.ss.xpence.dataaccess.PreferencesDAO;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<String> objects = new ArrayList<String>();
/*
		List<Pair<String, String>> r = new PreferencesDAO().queryAll(getBaseContext());
		for (Pair<String, String> pair : r) {
			objects.add(pair.first + " - " + pair.second);
		}*/

		MainScreenListingAdapter adapter = new MainScreenListingAdapter(this, objects);

		ListView listView = (ListView) findViewById(R.id.main_screen_listing);
		listView.setAdapter(adapter);
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
