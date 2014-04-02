package com.ss.xpence.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.util.IOUtils;

public class AccountsDAO extends AbstractDAO<AccountModel> {

	private static final String TABLE_NAME = "accounts";

	public List<AccountModel> queryAll(Context context) {
		init(context);

		Cursor c = database.query(TABLE_NAME, null, null, null, null, null, null);

		List<AccountModel> response = new ArrayList<AccountModel>();

		try {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					AccountModel model = new AccountModel();
					model.setAccountName(c.getString(c.getColumnIndexOrThrow("account_name")));
					model.setBankName(c.getString(c.getColumnIndexOrThrow("bank_name")));
					model.setAccountNumber(c.getString(c.getColumnIndexOrThrow("account_number")));
					model.setCardNumber(c.getString(c.getColumnIndexOrThrow("card_number")));
					model.setAccountId(c.getInt(c.getColumnIndexOrThrow("_id")));

					response.add(model);

					c.moveToNext();
				}
			}
		} finally {
			IOUtils.closeQuietly(c);
		}

		return response;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected ContentValues modelToContentValues(AccountModel model) {
		ContentValues contentValues = new ContentValues();

		contentValues.put("account_name", model.getAccountName());
		contentValues.put("bank_name", model.getBankName());
		contentValues.put("card_type", "DUM");
		contentValues.put("account_number", model.getAccountNumber());
		contentValues.put("card_number", model.getCardNumber());

		return contentValues;
	}

}
