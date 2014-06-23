package com.ss.xpence.content.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import com.ss.xpence.model.SMSModel;
import com.ss.xpence.util.IOUtils;

public class MessagesHandler {

	public static List<SMSModel> parseMessages(Context context, String sender) {
		return parseMessages(context, null, "address = ?", new String[] { sender });
	}

	public static List<SMSModel> parseMessages(Context context) {
		return parseMessages(context, null, null, null);
	}

	public static List<SMSModel> parseMessages(Context context, String sender, String messageIdMin) {
		if (messageIdMin == null) {
			return parseMessages(context, sender);
		}

		try {
			Long.parseLong(messageIdMin);
			return parseMessages(context, null, "address = ? and _id > ?", new String[] { sender, messageIdMin });
		} catch (NumberFormatException e) {
			return parseMessages(context, sender);
		}
	}

	public static List<String> fetchUniqueSenders(Context context) {
		Map<String, ArrayList<SMSModel>> all = new HashMap<String, ArrayList<SMSModel>>();
		fetchUniqueSenders(context, all);

		return new ArrayList<String>(all.keySet());
	}

	private static void fetchUniqueSenders(Context context, Map<String, ArrayList<SMSModel>> numbers) {
		Cursor c = null;

		try {
			c = new CursorLoader(context, Uri.parse("content://sms/inbox"), null, null, null, null).loadInBackground();

			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					ArrayList<SMSModel> messages = null;
					String number = c.getString(c.getColumnIndexOrThrow("address"));
					String body = c.getString(c.getColumnIndexOrThrow("body"));
					Date date = new Date(c.getLong(c.getColumnIndexOrThrow("date")));

					if (numbers.containsKey(number)) {
						messages = numbers.get(number);
					} else {
						messages = new ArrayList<SMSModel>();
						numbers.put(number, messages);
					}

					SMSModel message = new SMSModel();
					message.setMessage(body);
					message.setReceivedOn(date);
					messages.add(message);

					c.moveToNext();
				}
			}
		} finally {
			IOUtils.closeQuietly(c);
		}
	}

	private static List<SMSModel> parseMessages(Context context, String[] projection, String selection,
		String[] selectionArgs) {
		List<SMSModel> messages = new ArrayList<SMSModel>();
		Cursor c = null;

		try {
			c = new CursorLoader(context, Uri.parse("content://sms/inbox"), projection, selection, selectionArgs, null)
				.loadInBackground();

			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {

					String number = c.getString(c.getColumnIndexOrThrow("address"));
					String body = c.getString(c.getColumnIndexOrThrow("body"));
					Date date = new Date(c.getLong(c.getColumnIndexOrThrow("date")));
					long id = c.getLong(c.getColumnIndexOrThrow("_id"));

					SMSModel message = new SMSModel();
					message.setMessage(body);
					message.setReceivedOn(date);
					message.setId(id);
					message.setSender(number);
					messages.add(message);

					c.moveToNext();
				}
			}
		} finally {
			IOUtils.closeQuietly(c);
		}

		return messages;
	}

}
