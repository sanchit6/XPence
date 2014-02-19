package com.ss.xpence.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverterUtils {

	public static String safeToString(Object obj) {
		return obj == null ? null : obj.toString();
	}

	public static Double safeParseDouble(String s) {
		if (s == null) {
			return null;
		}
		try {
			return Double.parseDouble(s.replace(",", ""));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy HH:mm");
		return format.format(date);
	}

}
