package com.officine.client.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ClientConfig {

	private static final Properties PROPERTIES = new Properties();

	static {
		try (InputStream in = ClientConfig.class.getClassLoader().getResourceAsStream("client.properties")) {
			if (in != null) {
				PROPERTIES.load(in);
			}
		} catch (IOException ignored) {
		}
		String envBaseUrl = System.getenv("OFFICINE_API_BASE_URL");
		if (envBaseUrl != null && !envBaseUrl.isBlank()) {
			PROPERTIES.setProperty("officine.api.base-url", envBaseUrl);
		}
	}

	private ClientConfig() {
	}

	public static String apiBaseUrl() {
		String base = PROPERTIES.getProperty("officine.api.base-url", "http://localhost:9005/api/");
		return base.endsWith("/") ? base : base + "/";
	}
}
