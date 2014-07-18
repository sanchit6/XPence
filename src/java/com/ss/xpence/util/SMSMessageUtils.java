package com.ss.xpence.util;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Pair;

import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.content.handler.MessagesHandler;
import com.ss.xpence.dataaccess.PreferencesDAO;
import com.ss.xpence.dataaccess.SendersDAO;
import com.ss.xpence.dataaccess.TransactionsDAO;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.SMSModel;
import com.ss.xpence.model.SenderModel;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.parser.AbstractParser;
import com.ss.xpence.parser.ParserFactory;
import com.ss.xpence.view.IView;

public class SMSMessageUtils {

	private static SendersDAO sendersDAO;
	private static TransactionsDAO transactionsDAO;
	private static PreferencesDAO preferencesDAO;

	static {
		try {
			sendersDAO = ResourceManager.get(SendersDAO.class);
			transactionsDAO = ResourceManager.get(TransactionsDAO.class);
			preferencesDAO = ResourceManager.get(PreferencesDAO.class);
		} catch (ResourceException e) {
			throw new RuntimeException(e);
		}
	}

	public static void loadTransactionList(Context context, String bankName, String accountNo, String[] cardNos,
		long accId, List<TransactionModel> transactions) {
		List<SenderModel> allSenders = sendersDAO.queryAll(context);
		for (SenderModel senderModel : allSenders) {
			if (senderModel.getSelectedBank() != null && senderModel.getSelectedBank().equals(bankName)) {
				String sender = senderModel.getSender();

				Pair<String, String> pair = preferencesDAO.queryById(context,
					getMessageIdMaxPreferenceKey(sender, accId));

				List<SMSModel> messages = MessagesHandler.parseMessages(context, sender, pair == null ? "0"
					: pair.second);
				loadTransactionList(context, messages, senderModel, accountNo, cardNos, accId, transactions);
			}
		}
	}

	private static String getMessageIdMaxPreferenceKey(String sender, long accId) {
		return IView.MESSAGE_ID__MAX + "." + sender + ".Acc" + accId;
	}

	private static void loadTransactionList(Context context, List<SMSModel> messages, SenderModel senderModel,
		String accountNo, String[] cardNos, long accId, List<TransactionModel> transactions) {
		String sender = senderModel.getSender();

		List<AbstractParser> parsers;
		try {
			parsers = ParserFactory.makeParser(senderModel.getSelectedBank(), context);
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
					transactionsDAO.insert(context, item);

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
		preferencesDAO.insert(context, Pair.create(getMessageIdMaxPreferenceKey(sender, accId), String.valueOf(maxId)));
	}

}
