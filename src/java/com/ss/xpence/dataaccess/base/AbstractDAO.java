package com.ss.xpence.dataaccess.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ss.xpence.util.IOUtils;

public abstract class AbstractDAO<T> implements GenericDAO {
	protected SQLiteDatabase database;
	static Properties props = new Properties();

	protected void init(Context context) {
		if (database != null) {
			return;
		}

		InputStream stream = null;
		InputStreamReader reader = null;
		try {
			stream = context.getAssets().open("tables.properties");
			reader = new InputStreamReader(stream);
			props.load(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(stream);
		}

		DBHelper dbHelper = new DBHelper(this, context);
		database = dbHelper.getWritableDatabase();
	}

	public void clear(Context context) {
		init(context);

		database.beginTransaction();

		try {
			database.delete(getTableName(), null, null);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public void delete(Context context, String id) {
		init(context);

		database.beginTransaction();

		try {
			database.delete(getTableName(), "_id = ?", new String[] { id });

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public void delete(Context context) {
		init(context);

		database.beginTransaction();

		try {
			database.delete(getTableName(), null, null);

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public long insert(Context context, T model) {
		init(context);

		ContentValues contentValues = modelToContentValues(model);

		database.beginTransaction();

		try {
			long response = database.insertOrThrow(getTableName(), null, contentValues);
			database.setTransactionSuccessful();
			return response;
		} finally {
			database.endTransaction();
		}
	}

	protected abstract String getTableName();

	protected abstract ContentValues modelToContentValues(T model);

	public abstract List<T> queryAll(Context context);

	/**
	 * @param context
	 * @param filter
	 * @return List of Items
	 */
	public List<T> queryByFilter(Context context, Filter filter) {
		return null;
	}

	private static class DBHelper extends SQLiteOpenHelper {
		AbstractDAO<?> abstractDAO;

		DBHelper(AbstractDAO<?> abstractDAO, Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

			this.abstractDAO = abstractDAO;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for (Entry<Object, Object> entry : props.entrySet()) {
				db.execSQL((String) entry.getValue());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + abstractDAO.getTableName());
			onCreate(db);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			for (Entry<Object, Object> entry : props.entrySet()) {
				if (entry.getKey().equals(abstractDAO.getTableName())) {
					db.execSQL((String) entry.getValue());
				}
			}
		}

	}

	public static class Filter {
		private Map<String, Object> filters = new HashMap<String, Object>();

		public Object get(String key) {
			return filters.get(key);
		}

		public void put(String key, Object value) {
			filters.put(key, value);
		}

		public static Filter create(String key, Object value) {
			Filter f = new Filter();
			f.put(key, value);
			return f;
		}
	}

}
