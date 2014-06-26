package com.ss.xpence.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.content.res.AssetManager;

import com.mongodb.DBObject;
import com.ss.xpence.app.ResourceManager;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.exception.ResourceException;
import com.ss.xpence.util.IOUtils;

public class ParserFactory {
	private static boolean init = false;

	private static Properties props = new Properties();
	private static Map<String, List<AbstractParser>> parserCache = new HashMap<String, List<AbstractParser>>();

	public static List<AbstractParser> makeParser(String key, AssetManager assetManager) throws IOException {
		// initFromFile(assetManager);
		initFromMongo();

		if (parserCache.containsKey(key)) {
			return parserCache.get(key);
		}

		parserCache.put(key, new ArrayList<AbstractParser>());
		parserCache.get(key).addAll(newParser(key));
		return parserCache.get(key);
	}

	private static List<AbstractParser> newParser(String key) {
		int iterator = 1;

		List<AbstractParser> parsers = new ArrayList<AbstractParser>();

		while (true) {
			String x = key + "-" + iterator;
			String regexAmount = props.getProperty("amount-" + x);

			if (regexAmount == null) {
				break;
			}

			BaseRegexParser parser = new BaseRegexParser();
			parser.setAmountRegex(regexAmount);
			parser.setLocationRegex(props.getProperty("location-" + x));
			parser.setContainsRegex(props.getProperty("contains-" + x));

			parsers.add(parser);
			++iterator;
		}

		return parsers;
	}

	private static void initFromFile(AssetManager assetManager) throws IOException {
		if (init) {
			return;
		}

		InputStream stream = null;
		InputStreamReader reader = null;
		try {
			stream = assetManager.open("bank-regex.properties");
			reader = new InputStreamReader(stream);
			props.load(reader);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(stream);
		}

		init = true;
	}

	private static void initFromMongo() throws IOException {
		if (init) {
			return;
		}

		Map<String, Integer> indexer = new HashMap<String, Integer>();

		try {
			MongoConnector c = ResourceManager.get(MongoConnector.class);

			List<DBObject> oList = c.fetchCache();
			for (DBObject o : oList) {
				String bank = o.get("bank").toString();
				int index = indexer.containsKey(bank) ? indexer.get(bank) + 1 : 1;
				indexer.put(bank, index);

				props.put("amount-" + bank + "-" + index, o.get("amount"));
				props.put("location-" + bank + "-" + index, o.get("location"));
				props.put("contains-" + bank + "-" + index, o.get("contains"));
			}

		} catch (ResourceException e) {
			throw new IOException(e);
		}

		init = true;
	}
}
