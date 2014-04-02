package com.ss.xpence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ss.xpence.adapter.TransactionsAdapter;
import com.ss.xpence.content.handler.MessagesHandler;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.dataaccess.SendersDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.SMSModel;
import com.ss.xpence.model.SenderModel;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.model.meta.TransactionModelComparator;
import com.ss.xpence.parser.AbstractParser;
import com.ss.xpence.parser.ParserFactory;

public class TransactionsView extends FragmentActivity {

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
		return false;
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
			Fragment fragment = new TransactionsSectionFragment();
			Bundle args = new Bundle();
			args.putString(TransactionsSectionFragment.BANK_NAME, entities.get(i).getBankName());
			args.putString(TransactionsSectionFragment.ACCOUNT_NO, entities.get(i).getAccountNumber());
			args.putString(TransactionsSectionFragment.CARD_NO, entities.get(i).getCardNumber());
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

	public static class TransactionsSectionFragment extends Fragment {
		private List<TransactionModel> transactions;
		private SendersDAO sendersDAO = new SendersDAO();

		public TransactionsSectionFragment() {
		}

		public static final String BANK_NAME = "bankname";
		public static final String ACCOUNT_NO = "accountno";
		public static final String CARD_NO = "cardno";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ListView view = new ListView(getActivity());

			Bundle args = getArguments();
			String bankName = args.getString(BANK_NAME);
			String accountNo = args.getString(ACCOUNT_NO);
			String cardNo = args.getString(CARD_NO);

			loadTransactionList(bankName, container, accountNo, cardNo);

			Collections.sort(transactions, new TransactionModelComparator());

			TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactions);
			view.setAdapter(adapter);

			return view;
		}

		private void loadTransactionList(String bankName, ViewGroup container, String accountNo, String cardNo) {
			if (transactions != null) {
				return;
			}

			transactions = new ArrayList<TransactionModel>();

			List<SenderModel> allSenders = sendersDAO.queryAll(getActivity());
			for (SenderModel senderModel : allSenders) {
				if (senderModel.getSelectedBank() != null && senderModel.getSelectedBank().equals(bankName)) {
					List<?> messages = MessagesHandler.parseMessages(container.getContext(), senderModel.getSender());
					loadTransactionList(messages, senderModel.getSender(), accountNo, cardNo);
				}
			}
		}

		private void loadTransactionList(List<?> messages, String sender, String accountNo, String cardNo) {
			List<AbstractParser> parsers;
			try {
				parsers = ParserFactory.makeParser(sender, this.getResources().getAssets());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (parsers == null || parsers.isEmpty()) {
				return;
			}

			for (Object o : messages) {
				if (!(o instanceof SMSModel)) {
					continue;
				}

				SMSModel msg = (SMSModel) o;

				for (AbstractParser parser : parsers) {
					if (!parser.canParse(msg.getMessage(), accountNo, cardNo)) {
						continue;
					}

					TransactionModel item = (TransactionModel) parser.parse(msg.getMessage());

					if (item == null) {
						continue;
					}

					item.setDate(msg.getReceivedOn());
					item.setParentModel(msg);
					transactions.add(item);
					break;
				}
			}
		}

	}
}
