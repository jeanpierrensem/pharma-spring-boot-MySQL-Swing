package com.officine.client.auth;

import java.util.prefs.Preferences;

public final class TokenStorage {

	private static final String PREF_NODE = "com.officine.client.tokens";
	private static final String ACCESS = "accessToken";
	private static final String REFRESH = "refreshToken";
	private static final String EXPIRY = "expirationEpochMs";
	private static final String USERNAME = "username";
	private static final String REMEMBER = "rememberMe";

	private final Preferences preferences = Preferences.userRoot().node(PREF_NODE);

	public void save(String accessToken, String refreshToken, long expirationEpochMs, String username, boolean rememberMe) {
		if (rememberMe) {
			preferences.put(ACCESS, accessToken);
			preferences.put(REFRESH, refreshToken);
			preferences.putLong(EXPIRY, expirationEpochMs);
			preferences.put(USERNAME, username);
			preferences.putBoolean(REMEMBER, true);
		} else {
			clearPersistent();
		}
	}

	public boolean isRememberMeEnabled() {
		return preferences.getBoolean(REMEMBER, false);
	}

	public String loadAccessToken() {
		return preferences.get(ACCESS, null);
	}

	public String loadRefreshToken() {
		return preferences.get(REFRESH, null);
	}

	public long loadExpirationEpochMs() {
		return preferences.getLong(EXPIRY, 0L);
	}

	public String loadUsername() {
		return preferences.get(USERNAME, null);
	}

	public void clearPersistent() {
		preferences.remove(ACCESS);
		preferences.remove(REFRESH);
		preferences.remove(EXPIRY);
		preferences.remove(USERNAME);
		preferences.remove(REMEMBER);
	}
}
