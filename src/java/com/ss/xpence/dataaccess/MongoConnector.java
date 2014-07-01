package com.ss.xpence.dataaccess;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.os.AsyncTask;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.ss.xpence.Main;
import com.ss.xpence.adapter.LogAdapter;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.ParsersModel;

public class MongoConnector {

	private String uri = "mongodb://user1:s12345s@ds053139.mongolab.com:53139/sanchit_test";
	private static final List<DBObject> _cache = new ArrayList<DBObject>();

	private Semaphore _lock = new Semaphore(1);

	public boolean isFetchInProgress() {
		return _lock.availablePermits() == 0;
	}

	public List<DBObject> doFetchFromCache() {
		if (!_lock.tryAcquire()) {
			return null;
		}
		// _lock.availablePermits()
		try {
			return Collections.unmodifiableList(_cache);
		} finally {
			_lock.release();
		}
	}

	public void doFetchInBackground(final Context context) throws InterruptedException {
		_lock.acquire();
		// _lock.availablePermits()

		new AsyncTask<String, Void, List<DBObject>>() {

			@Override
			protected List<DBObject> doInBackground(String... params) {
				return fetch(context);
			}

			protected void onPostExecute(List<DBObject> result) {
				_cache.clear();
				_cache.addAll(result);
				_lock.release();
			}
		}.execute("");
	}

	public void doFetchFromCacheAndUpdateAdapter(final LogAdapter adapter) {
		new AsyncTask<String, Void, List<String>>() {

			@Override
			protected List<String> doInBackground(String... params) {
				List<DBObject> objects;
				objects = doFetchFromCache();
				List<String> z = new ArrayList<String>();

				for (DBObject o : objects) {
					z.add(o.toString());
				}

				return z;
			}

			protected void onPostExecute(List<String> result) {
				adapter.addAll(result);
				adapter.notifyDataSetChanged();
			}
		}.execute("");
	}

	private List<DBObject> fetch(Context context) {
		MongoClient client;
		try {
			client = new MongoClient(new MongoClientURI(uri));
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		DBCursor cursor = client.getDB("sanchit_test").getCollection("app_parsers").find();

		List<DBObject> response = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			DBObject _x = cursor.next();
			response.add(_x);

			try {
				ParsersDAO parsersDAO = ResourceManager.get(ParsersDAO.class);
				ParsersModel model = ParsersModel.newModel(_x);
				parsersDAO.insert(context, model );
			} catch (ResourceException e) {
				throw new RuntimeException(e);
			}
		}

		cursor.close();
		client.close();

		return response;
	}
}
