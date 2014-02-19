package com.ss.xpence.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.model.SenderModel;
import com.ss.xpence.util.IOUtils;

public class SendersDAO extends AbstractDAO<SenderModel> {

	private static final String TABLE_NAME = "senders";

	public List<SenderModel> queryAll(Context context) {
		init(context);

		Cursor c = database.query(TABLE_NAME, null, null, null, null, null, null);

		List<SenderModel> response = new ArrayList<SenderModel>();

		try {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					SenderModel model = new SenderModel();
					model.setSender(c.getString(c.getColumnIndexOrThrow("sender_name")));
					model.setSelectedBank(c.getString(c.getColumnIndexOrThrow("bank_name")));

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
	protected ContentValues modelToContentValues(SenderModel model) {
		ContentValues contentValues = new ContentValues();

		contentValues.put("sender_name", model.getSender());
		contentValues.put("bank_name", model.getSelectedBank());

		return contentValues;
	}

}
