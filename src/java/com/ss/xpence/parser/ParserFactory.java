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

import com.ss.xpence.util.IOUtils;

public class ParserFactory {
	private static boolean init = false;

	private static Properties props = new Properties();
	private static Map<String, List<AbstractParser>> parserCache = new HashMap<String, List<AbstractParser>>();

	public static List<AbstractParser> makeParser(String key, AssetManager assetManager) throws IOException {
		init(assetManager);

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

	private static void init(AssetManager assetManager) throws IOException {
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

}
