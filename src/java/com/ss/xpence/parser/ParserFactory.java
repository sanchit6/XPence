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

	public static List<AbstractParser> makeParser(String sender, AssetManager assetManager) throws IOException {
		init(assetManager);

		if (parserCache.containsKey(sender)) {
			return parserCache.get(sender);
		}

		parserCache.put(sender, new ArrayList<AbstractParser>());
		parserCache.get(sender).addAll(newParser(sender));
		return parserCache.get(sender);
	}

	private static List<AbstractParser> newParser(String sender) {
		int iterator = 1;

		List<AbstractParser> parsers = new ArrayList<AbstractParser>();

		while (true) {
			String key = sender + "-" + iterator;
			String regexAmount = props.getProperty("amount-" + key);

			if (regexAmount == null) {
				break;
			}

			BaseRegexParser parser = new BaseRegexParser();
			parser.setAmountRegex(regexAmount);
			parser.setLocationRegex(props.getProperty("location-" + key));
			parser.setContainsRegex(props.getProperty("contains-" + key));

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
			stream = assetManager.open("sender-regex.properties");
			reader = new InputStreamReader(stream);
			props.load(reader);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(stream);
		}

		init = true;
	}

}
