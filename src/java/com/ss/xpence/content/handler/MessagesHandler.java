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
		Cursor c = new CursorLoader(context, Uri.parse("content://sms/inbox"), null, null, null, null)
			.loadInBackground();
		List<SMSModel> messages = new ArrayList<SMSModel>();

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {

				String number = c.getString(c.getColumnIndexOrThrow("address"));
				String body = c.getString(c.getColumnIndexOrThrow("body"));
				Date date = new Date(c.getLong(c.getColumnIndexOrThrow("date")));

				if (!sender.equals(number)) {
					c.moveToNext();
					continue;
				}

				SMSModel message = new SMSModel();
				message.setMessage(body);
				message.setReceivedOn(date);
				messages.add(message);

				c.moveToNext();
			}
		}

		IOUtils.closeQuietly(c);
		return messages;
	}

	public static List<String> fetchUniqueSenders(Context context) {
		Map<String, ArrayList<SMSModel>> all = new HashMap<String, ArrayList<SMSModel>>();
		fetchUniqueSenders(context, all);

		return new ArrayList<String>(all.keySet());
	}

	public static void fetchUniqueSenders(Context context, Map<String, ArrayList<SMSModel>> numbers) {
		Cursor c = new CursorLoader(context, Uri.parse("content://sms/inbox"), null, null, null, null)
			.loadInBackground();

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

		IOUtils.closeQuietly(c);
	}

}
