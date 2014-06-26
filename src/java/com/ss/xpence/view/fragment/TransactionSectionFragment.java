package com.ss.xpence.view.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ss.xpence.adapter.TransactionsAdapter;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.content.handler.MessagesHandler;
import com.ss.xpence.dataaccess.PreferencesDAO;
import com.ss.xpence.dataaccess.SendersDAO;
import com.ss.xpence.dataaccess.TransactionsDAO;
import com.ss.xpence.dataaccess.base.AbstractDAO.Filter;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.SMSModel;
import com.ss.xpence.model.SenderModel;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.model.meta.TransactionModelComparator;
import com.ss.xpence.parser.AbstractParser;
import com.ss.xpence.parser.ParserFactory;
import com.ss.xpence.view.IView;

public class TransactionSectionFragment extends Fragment implements IView {
	private List<TransactionModel> transactions;

	private SendersDAO sendersDAO;
	private TransactionsDAO transactionsDAO;
	private PreferencesDAO preferencesDAO;

	public TransactionSectionFragment() throws ResourceException {
		transactions = new ArrayList<TransactionModel>();

		sendersDAO = ResourceManager.get(SendersDAO.class);
		transactionsDAO = ResourceManager.get(TransactionsDAO.class);
		preferencesDAO = ResourceManager.get(PreferencesDAO.class);
	}

	public static final String BANK_NAME = "bankname";
	public static final String ACCOUNT_NO = "accountno";
	public static final String ACCOUNT_ID = "accountid";
	public static final String CARD_NO = "cardno";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		String bankName = args.getString(BANK_NAME);
		String accountNo = args.getString(ACCOUNT_NO);
		long accId = args.getLong(ACCOUNT_ID);
		String[] cardNos = args.getString(CARD_NO).split(",");

		// First load the transactions that have already been parsed
		transactions = transactionsDAO.queryByFilter(getActivity(),
			Filter.create(TransactionsDAO.ACCOUNT_ID, Long.valueOf(accId)));

		// Second parse the new sms that have arrived
		loadTransactionList(bankName, accountNo, cardNos, accId);

		Collections.sort(transactions, new TransactionModelComparator());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView view = new ListView(getActivity());

		TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactions);
		view.setAdapter(adapter);

		return view;
	}

	private void loadTransactionList(String bankName, String accountNo, String[] cardNos, long accId) {
		List<SenderModel> allSenders = sendersDAO.queryAll(getActivity());
		for (SenderModel senderModel : allSenders) {
			if (senderModel.getSelectedBank() != null && senderModel.getSelectedBank().equals(bankName)) {
				String sender = senderModel.getSender();

				Pair<String, String> pair = preferencesDAO.queryById(getActivity(),
					getMessageIdMaxPreferenceKey(sender, accId));

				List<SMSModel> messages = MessagesHandler.parseMessages(getActivity(), sender, pair == null ? "0"
					: pair.second);
				loadTransactionList(messages, senderModel, accountNo, cardNos, accId);
			}
		}
	}

	private String getMessageIdMaxPreferenceKey(String sender, long accId) {
		return MESSAGE_ID__MAX + "." + sender + ".Acc" + accId;
	}

	private void loadTransactionList(List<SMSModel> messages, SenderModel senderModel, String accountNo,
		String[] cardNos, long accId) {
		String sender = senderModel.getSender();

		List<AbstractParser> parsers;
		try {
			parsers = ParserFactory.makeParser(senderModel.getSelectedBank(), this.getResources().getAssets());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (parsers == null || parsers.isEmpty() || messages == null || messages.isEmpty()) {
			return;
		}

		long maxId = 0;

		for (SMSModel msg : messages) {
			for (AbstractParser parser : parsers) {
				boolean done = false;

				for (String cardNo : cardNos) {
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

					// Should at this stage persist it to database
					item.setSenderModel(senderModel);
					item.setAccountModel(AccountModel.create(accId));
					transactionsDAO.insert(getActivity(), item);

					done = true;
					break;
				}

				if (done) {
					break;
				}
			}

			if (msg.getId() > maxId) {
				maxId = msg.getId();
			}
		}

		// Persist the max sms id parsed as a preference for given sender
		preferencesDAO.insert(getActivity(),
			Pair.create(getMessageIdMaxPreferenceKey(sender, accId), String.valueOf(maxId)));
	}
}
