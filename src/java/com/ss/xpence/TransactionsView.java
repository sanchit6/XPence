package com.ss.xpence;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.dataaccess.PreferencesDAO;
import com.ss.xpence.dataaccess.TransactionsDAO;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.ConverterUtils;
import com.ss.xpence.view.fragment.TransactionSectionFragment;

public class TransactionsView extends FragmentActivity {

	private TransactionsDAO transactionsDAO;
	private PreferencesDAO preferencesDAO;

	public TransactionsView() throws ResourceException {
		transactionsDAO = ResourceManager.get(TransactionsDAO.class);
		preferencesDAO = ResourceManager.get(PreferencesDAO.class);
	}

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_transactions_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.transaction_view_clear_db:
				transactionsDAO.clear(getBaseContext());
				preferencesDAO.clear(getBaseContext());
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private List<AccountModel> entities;
		private AccountsDAO accountsDAO = new AccountsDAO();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

			entities = accountsDAO.queryAll(getBaseContext());
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment;
			try {
				fragment = new TransactionSectionFragment();
			} catch (ResourceException e) {
				throw new RuntimeException(e);
			}
			Bundle args = new Bundle();
			AccountModel accountModel = entities.get(i);

			args.putString(TransactionSectionFragment.BANK_NAME, accountModel.getBankName());
			args.putString(TransactionSectionFragment.ACCOUNT_NO, accountModel.getAccountNumber());
			args.putString(TransactionSectionFragment.CARD_NO,
				ConverterUtils.accumulate(accountModel.getCardNumbers(), ","));
			args.putLong(TransactionSectionFragment.ACCOUNT_ID, accountModel.getAccountId());

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return entities.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return entities.get(position).getAccountName();
		}
	}

}
