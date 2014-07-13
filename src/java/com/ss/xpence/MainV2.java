package com.ss.xpence;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.xpence.adapter.DrawerItemAdapter;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.dataaccess.ParsersDAO;
import com.ss.xpence.exception.ResourceException;

public class MainV2 extends Activity {
	private ListView mDrawerList;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private String[] mItems = { "Transactions", "Accounts", "Senders", "Parsers" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ss.xpence.R.layout.activity_main_v2);

		mDrawerList = (ListView) findViewById(com.ss.xpence.R.id.main_left_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new DrawerItemAdapter(this, Arrays.asList(mItems)));

		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						MongoConnector c;
						try {
							c = ResourceManager.get(MongoConnector.class);
							if (c.isFetchInProgress()) {
								Toast.makeText(getApplicationContext(), "Loading Parsers.. Please Wait!!",
									Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (ResourceException e) {
							throw new RuntimeException(e);
						}

						Intent intent = new Intent(getBaseContext(), TransactionsView.class);
						startActivity(intent);
						break;
					case 1:
						intent = new Intent(getBaseContext(), AccountsView.class);
						startActivity(intent);
						break;
					case 2:
						intent = new Intent(getBaseContext(), SendersManager.class);
						startActivity(intent);
						break;
					default:
						break;
				}
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer3, R.string.app_name,
			R.string.app_name) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

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

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(com.ss.xpence.R.menu.activity_main_v2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		boolean isVisible = mDrawerLayout.isDrawerOpen(mDrawerList);
		if(isVisible) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}
}
