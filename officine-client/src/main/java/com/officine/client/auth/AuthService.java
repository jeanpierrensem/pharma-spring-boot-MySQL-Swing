package com.officine.client.auth;

import com.officine.client.api.ApiClient;
import com.officine.client.api.ApiException;
import com.officine.client.model.CurrentUser;
import com.officine.client.model.JwtAuthResponse;
import java.util.Map;

public final class AuthService {

	private static AuthService instance;

	private final ApiClient apiClient = ApiClient.getInstance();
	private final SessionManager sessionManager = SessionManager.getInstance();

	private AuthService() {
	}

	public static synchronized AuthService getInstance() {
		if (instance == null) {
			instance = new AuthService();
		}
		return instance;
	}

	public JwtAuthResponse login(String username, String password, boolean rememberMe) {
		Map<String, Object> payload = Map.of(
				"username", username,
				"password", password,
				"rememberMe", rememberMe);
		JwtAuthResponse response = apiClient.postPublic("auth/login", payload, JwtAuthResponse.class);
		applySession(response, rememberMe);
		return response;
	}

	public JwtAuthResponse register(String username, String name, String email, String password) {
		Map<String, Object> payload = Map.of(
				"username", username,
				"name", name,
				"email", email,
				"password", password);
		JwtAuthResponse response = apiClient.postPublic("auth/register", payload, JwtAuthResponse.class);
		applySession(response, false);
		return response;
	}

	public CurrentUser fetchCurrentUser() {
		ensureValidAccessToken();
		CurrentUser user = apiClient.get("users/me", CurrentUser.class);
		sessionManager.applyLogin(
				new JwtAuthResponse(
						sessionManager.accessToken().orElse(null),
						sessionManager.refreshToken().orElse(null),
						"Bearer",
						user.login(),
						user.roles(),
						null),
				user,
				false);
		return user;
	}

	public void logout() {
		try {
			String refresh = sessionManager.refreshToken().orElse(null);
			if (refresh != null) {
				apiClient.postNoContent("auth/logout", Map.of("refreshToken", refresh), true);
			}
		} catch (ApiException ignored) {
		} finally {
			sessionManager.clear();
		}
	}

	public boolean tryRestoreSession() {
		if (!sessionManager.restoreRememberMeSession()) {
			return false;
		}
		try {
			fetchCurrentUser();
			return true;
		} catch (ApiException ex) {
			tryRefreshToken();
			try {
				fetchCurrentUser();
				return true;
			} catch (ApiException ignored) {
				sessionManager.clear();
				return false;
			}
		}
	}

	private void applySession(JwtAuthResponse response, boolean rememberMe) {
		CurrentUser user = new CurrentUser(
				null,
				response.username(),
				response.username(),
				null,
				true,
				null,
				response.roles());
		sessionManager.applyLogin(response, user, rememberMe);
		try {
			CurrentUser loaded = apiClient.get("users/me", CurrentUser.class);
			sessionManager.applyLogin(response, loaded, rememberMe);
		} catch (ApiException ignored) {
		}
	}

	private void ensureValidAccessToken() {
		if (sessionManager.isAccessTokenExpired()) {
			tryRefreshToken();
		}
	}

	private void tryRefreshToken() {
		String refresh = sessionManager.refreshToken().orElseThrow(
				() -> new ApiException(401, "Session expirée — reconnectez-vous"));
		JwtAuthResponse refreshed = apiClient.postPublic("auth/refresh", Map.of("refreshToken", refresh), JwtAuthResponse.class);
		sessionManager.applyLogin(refreshed, sessionManager.currentUser(), sessionManager.accessToken().isPresent());
	}
}
