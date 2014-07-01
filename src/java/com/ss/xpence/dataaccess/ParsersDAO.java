package com.ss.xpence.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.model.ParsersModel;
import com.ss.xpence.util.IOUtils;

public class ParsersDAO extends AbstractDAO<ParsersModel> {

	@Override
	protected String getTableName() {
		return "parsers";
	}

	@Override
	protected ContentValues modelToContentValues(ParsersModel model) {
		ContentValues contentValues = new ContentValues();

		contentValues.put("bank_name", model.getBank());
		contentValues.put("contains_regex", model.getContainsRegex());
		contentValues.put("amount_regex", model.getAmountRegex());
		contentValues.put("location_regex", model.getLocationRegex());

		return contentValues;
	}

	@Override
	public List<ParsersModel> queryAll(Context context) {
		init(context);

		Cursor c = database.query(getTableName(), null, null, null, null, null, null);

		List<ParsersModel> response = new ArrayList<ParsersModel>();

		try {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					ParsersModel model = new ParsersModel();
					model.setBank(c.getString(c.getColumnIndexOrThrow("bank_name")));
					model.setContainsRegex(c.getString(c.getColumnIndexOrThrow("contains_regex")));
					model.setAmountRegex(c.getString(c.getColumnIndexOrThrow("amount_regex")));
					model.setLocationRegex(c.getString(c.getColumnIndexOrThrow("location_regex")));
					model.setId(c.getInt(c.getColumnIndexOrThrow("_id")));

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
