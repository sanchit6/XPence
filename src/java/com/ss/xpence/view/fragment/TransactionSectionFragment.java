package com.ss.xpence.view.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ss.xpence.adapter.TransactionsAdapter;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.TransactionsDAO;
import com.ss.xpence.dataaccess.base.AbstractDAO.Filter;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.model.meta.TransactionModelComparator;
import com.ss.xpence.util.SMSMessageUtils;
import com.ss.xpence.view.IView;

public class TransactionSectionFragment extends Fragment implements IView {
	private List<TransactionModel> transactions;

	private TransactionsDAO transactionsDAO;

	public TransactionSectionFragment() throws ResourceException {
		transactions = new ArrayList<TransactionModel>();

		transactionsDAO = ResourceManager.get(TransactionsDAO.class);
	}

	public static final String BANK_NAME = "bankname";
	public static final String ACCOUNT_NO = "accountno";
	public static final String ACCOUNT_ID = "accountid";
	public static final String CARD_NO = "cardno";
	public static final String ROLLOVER_DATE = "rolloverdate";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		String bankName = args.getString(BANK_NAME);
		String accountNo = args.getString(ACCOUNT_NO);
		long accId = args.getLong(ACCOUNT_ID);
		String[] cardNos = args.getString(CARD_NO).split(",");
		int rolloverDate = args.getInt(ROLLOVER_DATE);

		// First load the transactions that have already been parsed
		transactions = transactionsDAO.queryByFilter(getActivity(),
			Filter.create(TransactionsDAO.ACCOUNT_ID, Long.valueOf(accId)));

		// Second parse the new sms that have arrived
		SMSMessageUtils.loadTransactionList(getActivity(), bankName, accountNo, cardNos, accId, transactions);

		Collections.sort(transactions, new TransactionModelComparator());

		insertRolloverPoints(rolloverDate);

		Collections.sort(transactions, new TransactionModelComparator());

		sumUpAtRolloverPoints();
	}

	private void sumUpAtRolloverPoints() {
		BigDecimal sum = new BigDecimal(0);
		for (int i = transactions.size() - 1; i >= 0; i--) {
			TransactionModel t = transactions.get(i);

			if (TransactionsAdapter.STATEMENT.equals(t.getLocation())) {
				t.setAmount(sum.doubleValue());
				sum = new BigDecimal(0);
			} else {
				BigDecimal augmend = new BigDecimal(t.getAmount() != null ? t.getAmount() : 0d);
				sum = sum.add(augmend);
			}
		}
	}

	private void insertRolloverPoints(int rolloverDate) {
		Date latestDate = transactions.get(0).getDate();
		Date oldestDate = transactions.get(transactions.size() - 1).getDate();

		int ul = new Date().getMonth() > latestDate.getMonth() ? new Date().getMonth() : latestDate.getMonth();

		for (int i = oldestDate.getMonth(); i <= ul; i++) {
			TransactionModel t = new TransactionModel();
			t.setDate(new Date(oldestDate.getYear(), i, rolloverDate));
			t.setLocation(TransactionsAdapter.STATEMENT);
			transactions.add(t);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView view = new ListView(getActivity());

		TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactions);
		view.setAdapter(adapter);

		return view;
	}

}
