package com.officine.client.auth;

import com.officine.client.model.CurrentUser;
import com.officine.client.model.JwtAuthResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class SessionManager {

	private static SessionManager instance;

	private JwtAuthResponse authResponse;
	private CurrentUser currentUser;
	private final TokenStorage tokenStorage = new TokenStorage();

	private SessionManager() {
	}

	public static synchronized SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}

	public void applyLogin(JwtAuthResponse response, CurrentUser user, boolean rememberMe) {
		this.authResponse = response;
		this.currentUser = user;
		if (response.expiration() != null) {
			tokenStorage.save(
					response.accessToken(),
					response.refreshToken(),
					response.expiration().toEpochMilli(),
					response.username(),
					rememberMe);
		}
	}

	public Optional<String> accessToken() {
		if (authResponse != null && authResponse.accessToken() != null) {
			return Optional.of(authResponse.accessToken());
		}
		if (tokenStorage.isRememberMeEnabled()) {
			return Optional.ofNullable(tokenStorage.loadAccessToken());
		}
		return Optional.empty();
	}

	public Optional<String> refreshToken() {
		if (authResponse != null && authResponse.refreshToken() != null) {
			return Optional.of(authResponse.refreshToken());
		}
		return Optional.ofNullable(tokenStorage.loadRefreshToken());
	}

	public boolean isAuthenticated() {
		return accessToken().isPresent() && !isAccessTokenExpired();
	}

	public boolean isAccessTokenExpired() {
		Instant expiry = authResponse != null ? authResponse.expiration() : null;
		if (expiry == null && tokenStorage.isRememberMeEnabled()) {
			long epoch = tokenStorage.loadExpirationEpochMs();
			if (epoch > 0) {
				expiry = Instant.ofEpochMilli(epoch);
			}
		}
		return expiry != null && expiry.isBefore(Instant.now());
	}

	public List<String> roles() {
		if (currentUser != null && currentUser.roles() != null) {
			return currentUser.roles();
		}
		if (authResponse != null && authResponse.roles() != null) {
			return authResponse.roles();
		}
		return List.of();
	}

	public boolean hasRole(String role) {
		return roles().stream().anyMatch(r -> r.equalsIgnoreCase(role));
	}

	public CurrentUser currentUser() {
		return currentUser;
	}

	public void clear() {
		authResponse = null;
		currentUser = null;
		tokenStorage.clearPersistent();
	}

	public boolean restoreRememberMeSession() {
		if (!tokenStorage.isRememberMeEnabled()) {
			return false;
		}
		String access = tokenStorage.loadAccessToken();
		if (access == null || access.isBlank()) {
			return false;
		}
		authResponse = new JwtAuthResponse(
				access,
				tokenStorage.loadRefreshToken(),
				"Bearer",
				tokenStorage.loadUsername(),
				List.of(),
				Instant.ofEpochMilli(tokenStorage.loadExpirationEpochMs()));
		return true;
	}
}
