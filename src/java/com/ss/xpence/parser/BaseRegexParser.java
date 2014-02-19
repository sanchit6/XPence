package com.ss.xpence.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ss.xpence.model.BaseModel;
import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.util.ConverterUtils;

public class BaseRegexParser extends AbstractParser {
	private String amountRegex;
	private String locationRegex;
	private String uniqueIdRegex;

	@Override
	public BaseModel parse(String message) {
		TransactionModel model = new TransactionModel();

		model.setAmount(ConverterUtils.safeParseDouble(match(message, amountRegex)));
		model.setLocation(match(message, locationRegex));

		return model;
	}

	private String match(String message, String pattern) {
		if (pattern == null || message == null) {
			return null;
		}

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(message);
		if (m.find()) {
			return m.group(1);
		}

		return null;
	}

	public void setAmountRegex(String amountRegex) {
		this.amountRegex = amountRegex;
	}

	public void setLocationRegex(String locationRegex) {
		this.locationRegex = locationRegex;
	}

	public void setUniqueIdRegex(String uniqueIdRegex) {
		this.uniqueIdRegex = uniqueIdRegex;
	}

}
