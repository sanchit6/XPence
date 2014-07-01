package com.ss.xpence.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.mongodb.DBObject;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.dataaccess.ParsersDAO;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.model.ParsersModel;

public class ParserFactory {
	private static boolean init = false;

	private static Map<String, List<AbstractParser>> parserCache = new HashMap<String, List<AbstractParser>>();

	public static List<AbstractParser> makeParser(String key, Context context) throws IOException {
		init(context);
		return parserCache.get(key);
	}

	private static void init(Context context) throws IOException {
		if (init) {
			return;
		}

		try {
			ParsersDAO parsersDAO = ResourceManager.get(ParsersDAO.class);
			List<ParsersModel> models = parsersDAO.queryAll(context);

			if (models != null && !models.isEmpty()) {
				for (ParsersModel model : models) {
					BaseRegexParser parser = new BaseRegexParser();
					parser.setAmountRegex(model.getAmountRegex());
					parser.setLocationRegex(model.getLocationRegex());
					parser.setContainsRegex(model.getContainsRegex());

					String key = model.getBank();
					if (!parserCache.containsKey(key)) {
						parserCache.put(key, new ArrayList<AbstractParser>());
					}

					parserCache.get(key).add(parser);
				}
			} else {
				MongoConnector c = ResourceManager.get(MongoConnector.class);

				List<DBObject> oList = c.doFetchFromCache();
				for (DBObject o : oList) {
					String key = o.get("bank").toString();

					BaseRegexParser parser = new BaseRegexParser();
					parser.setAmountRegex(o.get("amount").toString());
					parser.setLocationRegex(o.get("location").toString());
					parser.setContainsRegex(o.get("contains").toString());

					if (!parserCache.containsKey(key)) {
						parserCache.put(key, new ArrayList<AbstractParser>());
					}

					parserCache.get(key).add(parser);
				}
			}
		} catch (ResourceException e) {
			throw new IOException(e);
		}

		init = true;
	}
}
