package edu.ucla.wise.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpUtils {

	private static final String ENCODING = "UTF-8";

	/**
	 * Create a URL, encoding the parameters
	 */
	public static CharSequence createURL(String uri, String[][] parameters) {
		StringBuilder url = new StringBuilder();
		url.append(uri);
		if (parameters != null) {
			url.append('?');
			url.append(getURLParameters(parameters));
		}

		return url;
	}

	public static CharSequence getURLParameters(String[][] parameters) {

		StringBuilder urlParameters = new StringBuilder();

		if (parameters != null) {
			for (String[] p : parameters) {

				// Check that the key,value pair are not null.
				if (p[0] == null) {
					throw new IllegalArgumentException("A parameter was null.");
				}
				urlParameters.append(p[0]).append('=');
				// if a value is null, send key = empty string
				if (p[1] != null) {
					try {
						p[1] = URLEncoder.encode(p[1], ENCODING);
					} catch (UnsupportedEncodingException e) {
						// ignored
					}
					urlParameters.append(p[1]);
				}
				urlParameters.append('&');
			}
			// remove last '&' or '?' if array size is 0
			urlParameters.setLength(urlParameters.length() - 1);
		}

		return urlParameters;
	}
}
