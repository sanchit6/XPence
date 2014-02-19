package com.ss.xpence.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

	public static void closeQuietly(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

}
