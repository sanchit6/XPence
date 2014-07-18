package com.ss.xpence.view.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.ss.xpence.R;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.card.thisweek.ThisWeekCardAdapter;
import com.ss.xpence.card.thisweek.ThisWeekModel;
import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.dataaccess.TransactionsDAO;
import com.ss.xpence.dataaccess.base.AbstractDAO.Filter;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.util.DateUtils;
import com.ss.xpence.view.compoundview.SimpleCard;

public class HomeFragment extends Fragment {

	private TransactionsDAO transactionsDAO;
	private AccountsDAO accountsDAO;

	public HomeFragment() throws ResourceException {
		transactionsDAO = ResourceManager.get(TransactionsDAO.class);
		accountsDAO = ResourceManager.get(AccountsDAO.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);

		SimpleCard thisWeekCard = (SimpleCard) rootView.findViewById(R.id.card_this_week);
		thisWeekCard.setAdapter(getThisWeekAdapter());

		return rootView;
	}

	private ListAdapter getThisWeekAdapter() {
		List<ThisWeekModel> objects = new ArrayList<ThisWeekModel>();

		// Load the list of entities
		List<AccountModel> entities = accountsDAO.queryAll(getActivity());

		for (AccountModel accountModel : entities) {
			long accId = accountModel.getAccountId();

			// Load the transactions that have already been parsed
			Filter filter = Filter.create(TransactionsDAO.ACCOUNT_ID, Long.valueOf(accId)).append(
				TransactionsDAO.MIN_DATE, DateUtils.getStartOfWeekDate());

			List<TransactionModel> transactions = transactionsDAO.queryByFilter(getActivity(), filter);

			ThisWeekModel model = new ThisWeekModel();
			model.setAccountName(accountModel.getAccountName());

			BigDecimal sum = new BigDecimal(0);
			for (TransactionModel t : transactions) {
				sum = sum.add(new BigDecimal(t.getAmount()));
			}

			model.setAmount(sum.doubleValue());
			objects.add(model);
		}

		ThisWeekCardAdapter adapter = new ThisWeekCardAdapter(getActivity(), objects);
		return adapter;
	}
}
