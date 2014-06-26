package com.ss.xpence.dataaccess;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.os.AsyncTask;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.ss.xpence.adapter.LogAdapter;

public class MongoConnector {

	private String uri = "mongodb://user1:s12345s@ds053139.mongolab.com:53139/sanchit_test";
	private static final List<DBObject> _cache = new ArrayList<DBObject>();

	private Semaphore _lock = new Semaphore(1);

	public List<DBObject> fetchCache() {
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

	public void asyncFetchAndCache() throws InterruptedException {
		_lock.acquire();
		// _lock.availablePermits()

		new AsyncTask<String, Void, List<DBObject>>() {

			@Override
			protected List<DBObject> doInBackground(String... params) {
				return fetch();
			}

			protected void onPostExecute(List<DBObject> result) {
				_cache.clear();
				_cache.addAll(result);
				_lock.release();
			}
		}.execute("");
	}

	public void fetch(final LogAdapter adapter) {
		new AsyncTask<String, Void, List<String>>() {

			@Override
			protected List<String> doInBackground(String... params) {
				List<DBObject> objects;
				objects = fetchCache();
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

	private List<DBObject> fetch() {
		MongoClient client;
		try {
			client = new MongoClient(new MongoClientURI(uri));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		DBCursor x = client.getDB("sanchit_test").getCollection("app_parsers").find();

		List<DBObject> z = new ArrayList<DBObject>();
		while (x.hasNext()) {
			z.add(x.next());
		}

		x.close();
		client.close();

		return z;
	}
}
