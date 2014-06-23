package com.ss.xpence.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.SMSModel;
import com.ss.xpence.model.SenderModel;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.util.IOUtils;

public class TransactionsDAO extends AbstractDAO<TransactionModel> {
	public static final String ACCOUNT_ID = "account_id";

	@Override
	protected String getTableName() {
		return "transactions";
	}

	@Override
	protected ContentValues modelToContentValues(TransactionModel model) {
		ContentValues contentValues = new ContentValues();

		contentValues.put("account_id", model.getAccountModel().getAccountId());
		contentValues.put("sender_id", model.getSenderModel().getSenderId());
		contentValues.put("message", model.getParentModel().getMessage());
		contentValues.put("comment", model.getComment());
		contentValues.put("date", model.getDate().getTime());
		contentValues.put("amount", model.getAmount());
		contentValues.put("currency", "INR");
		contentValues.put("desc", model.getLocation());

		return contentValues;
	}

	@Override
	public List<TransactionModel> queryAll(Context context) {
		return null;
	}

	@Override
	public List<TransactionModel> queryByFilter(Context context, Filter filter) {
		Long accountId = (Long) filter.get(ACCOUNT_ID);

		init(context);

		Cursor c = database.query(getTableName(), null, "account_id = ?", new String[] { String.valueOf(accountId) },
			null, null, null);

		List<TransactionModel> response = new ArrayList<TransactionModel>();

		try {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					TransactionModel model = new TransactionModel();
					model.setAccountModel(AccountModel.create(c.getLong(c.getColumnIndexOrThrow("account_id"))));
					model.setSenderModel(SenderModel.create(c.getLong(c.getColumnIndexOrThrow("sender_id"))));
					model.setParentModel(SMSModel.create(c.getString(c.getColumnIndexOrThrow("message"))));

					model.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
					model.setLocation(c.getString(c.getColumnIndexOrThrow("desc")));
					model.setComment(c.getString(c.getColumnIndexOrThrow("comment")));
					model.setAmount(c.getDouble(c.getColumnIndexOrThrow("amount")));
					model.setDate(new Date(c.getLong(c.getColumnIndexOrThrow("date"))));

					response.add(model);

					c.moveToNext();
				}
			}
		} finally {
			IOUtils.closeQuietly(c);
		}

		return response;
	}

}
