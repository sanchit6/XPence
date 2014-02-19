package com.ss.xpence.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.content.res.AssetManager;

import com.ss.xpence.util.IOUtils;

public class ParserFactory {
	private static boolean init = false;

	private static Properties props = new Properties();
	private static Map<String, AbstractParser> parserCache = new HashMap<String, AbstractParser>();

	public static AbstractParser makeParser(String sender, AssetManager assetManager) throws IOException {
		init(assetManager);

		if (parserCache.containsKey(sender)) {
			return parserCache.get(sender);
		}

		BaseRegexParser parser = new BaseRegexParser();
		parserCache.put(sender, parser);

		initParser(parser, sender);

		return parser;
	}

	private static void initParser(BaseRegexParser parser, String sender) {
		parser.setAmountRegex(props.getProperty("amount-" + sender));
		parser.setLocationRegex(props.getProperty("location-" + sender));
		parser.setUniqueIdRegex(props.getProperty("uniqueid-" + sender));
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
