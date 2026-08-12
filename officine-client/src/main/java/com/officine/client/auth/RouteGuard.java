package com.officine.client.auth;

public final class RouteGuard {

	private RouteGuard() {
	}

	public static boolean canAccessAdmin(SessionManager session) {
		return session.hasRole("ADMIN");
	}

	public static boolean canAccessUserArea(SessionManager session) {
		return session.hasRole("USER") || session.hasRole("ADMIN");
	}
}
