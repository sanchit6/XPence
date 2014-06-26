package com.ss.xpence.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Pair;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.util.IOUtils;

public class PreferencesDAO extends AbstractDAO<Pair<String, String>> {

	@Override
	protected String getTableName() {
		return "preferences";
	}

	@Override
	protected ContentValues modelToContentValues(Pair<String, String> model) {
		ContentValues contentValues = new ContentValues();

		contentValues.put("_id", model.first);
		contentValues.put("value", model.second);

		return contentValues;
	}

	@Override
	public List<Pair<String, String>> queryAll(Context context) {
		init(context);
		List<Pair<String, String>> r = new ArrayList<Pair<String, String>>();

		Cursor c = database.query(getTableName(), null, null, null, null, null, null);

		try {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					String value = c.getString(c.getColumnIndexOrThrow("value"));
					String key = c.getString(c.getColumnIndexOrThrow("_id"));
					r.add(Pair.create(key, value));
					c.moveToNext();
				}
			}
		} finally {
			IOUtils.closeQuietly(c);
		}

		return r;
	}

	public Pair<String, String> queryById(Context context, String key) {
		init(context);

		Cursor c = null;
		Pair<String, String> data = null;

		try {
			c = database.query(getTableName(), null, "_id = ?", new String[] { key }, null, null, null);

			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					String value = c.getString(c.getColumnIndexOrThrow("value"));
					data = Pair.create(key, value);
				}
			}

			return data;
		} finally {
			IOUtils.closeQuietly(c);
		}

	}

	@Override
	public long insert(Context context, Pair<String, String> model) {
		this.delete(context, model.first);

		return super.insert(context, model);
	}

}
