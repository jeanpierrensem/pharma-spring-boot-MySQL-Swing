package com.officine.losto.security;

import java.util.Locale;

/** Normalisation des logins (minuscules, trim) — évite les collisions MySQL insensibles à la casse. */
public final class LoginNormalizer {

	private LoginNormalizer() {
	}

	public static String normalize(String login) {
		if (login == null) {
			return null;
		}
		String trimmed = login.trim();
		if (trimmed.isEmpty()) {
			return trimmed;
		}
		return trimmed.toLowerCase(Locale.ROOT);
	}
}
