package com.ss.xpence.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ss.xpence.model.TransactionModel;
import com.ss.xpence.util.ConverterUtils;

public class BaseRegexParser extends AbstractParser {
	private String amountRegex;
	private String locationRegex;

	private String containsRegex;

	@Override
	public boolean canParse(String message, String accountNo, String cardNo) {
		if(message.contains("AUTO")) {
			System.out.println();
		}
		
		if (!match(message, containsRegex)) {
			return false;
		}

		if (match(message, lastFourDigitsAsRegex(accountNo)) || match(message, lastFourDigitsAsRegex(cardNo))) {
			return true;
		}

		return false;
	}

	private String lastFourDigitsAsRegex(String number) {
		return ".*" + (number.length() > 4 ? number.substring(number.length() - 4, number.length()) : number) + ".*";
	}

	@Override
	public TransactionModel parse(String message) {
		TransactionModel model = new TransactionModel();

		model.setAmount(ConverterUtils.safeParseDouble(fetch(message, amountRegex)));
		model.setLocation(fetch(message, locationRegex));

		return model;
	}

	private boolean match(String message, String pattern) {
		if (pattern == null || message == null) {
			return false;
		}

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		return r.matcher(message).matches();
	}

	private String fetch(String message, String pattern) {
		if (pattern == null || message == null) {
			return null;
		}

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(message);
		StringBuilder builder = new StringBuilder();
		if (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				builder.append(m.group(i)).append(", ");
			}

		}

		return builder.length() > 2 ? builder.substring(0, builder.length() - 2) : builder.toString();
	}

	public void setAmountRegex(String amountRegex) {
		this.amountRegex = amountRegex;
	}

	public void setLocationRegex(String locationRegex) {
		this.locationRegex = locationRegex;
	}

	public void setContainsRegex(String containsRegex) {
		this.containsRegex = containsRegex;
	}

}
