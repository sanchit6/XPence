package com.ss.xpence;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ss.xpence.adapter.LogAdapter;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.exception.ResourceException;

public class LogViewer extends Activity {

	private MongoConnector connector;

	public LogViewer() throws ResourceException {
		connector = ResourceManager.get(MongoConnector.class);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_viewer);

		List<String> objects = new ArrayList<String>();
		LogAdapter adapter = new LogAdapter(this, objects);

		connector.fetch(adapter);

		/*
		 * List<Pair<String, String>> r = new
		 * PreferencesDAO().queryAll(getBaseContext()); for (Pair<String,
		 * String> pair : r) { objects.add(pair.first + " - " + pair.second); }
		 */

		ListView listView = (ListView) findViewById(R.id.log_listing);
		listView.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_log_viewer, menu);
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

}
