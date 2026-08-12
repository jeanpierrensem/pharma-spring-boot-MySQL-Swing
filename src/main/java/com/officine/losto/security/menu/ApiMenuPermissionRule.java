package com.officine.losto.security.menu;

import org.springframework.http.HttpMethod;

/**
 * Règle API → pathCode habilitation ({@link MenuSecurityCatalog}).
 */
public record ApiMenuPermissionRule(
		HttpMethod method,
		String pathPattern,
		String pathCode,
		MatchMode matchMode) {

	public enum MatchMode {
		/** Le pathCode exact doit être dans le groupe. */
		EXACT,
		/** Le pathCode du groupe doit être égal ou être un descendant du pathCode écran. */
		SCREEN
	}

	public boolean matches(HttpMethod requestMethod, String requestPath) {
		if (method != null && method != requestMethod) {
			return false;
		}
		if (pathPattern.endsWith("/**")) {
			String prefix = pathPattern.substring(0, pathPattern.length() - 3);
			return requestPath.equals(prefix) || requestPath.startsWith(prefix + "/");
		}
		if (pathPattern.endsWith("/*")) {
			String prefix = pathPattern.substring(0, pathPattern.length() - 2);
			if (!requestPath.startsWith(prefix)) {
				return false;
			}
			String rest = requestPath.substring(prefix.length());
			return rest.isEmpty() || (rest.startsWith("/") && !rest.substring(1).contains("/"));
		}
		return requestPath.equals(pathPattern);
	}
}
