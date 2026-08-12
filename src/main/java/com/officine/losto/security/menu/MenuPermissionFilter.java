package com.officine.losto.security.menu;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Vérifie les habilitations menu (pathCode) pour les appels {@code /api/**} authentifiés.
 */
@Component
public class MenuPermissionFilter extends OncePerRequestFilter {

	private final MenuPermissionService menuPermissionService;

	public MenuPermissionFilter(MenuPermissionService menuPermissionService) {
		this.menuPermissionService = menuPermissionService;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		if (!path.startsWith("/api/")) {
			return true;
		}
		if (path.startsWith("/api/auth/")) {
			return true;
		}
		if ("/api/users/me".equals(path) && "GET".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		return false;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			filterChain.doFilter(request, response);
			return;
		}
		if (menuPermissionService.isAdmin(authentication)) {
			filterChain.doFilter(request, response);
			return;
		}

		HttpMethod method = HttpMethod.valueOf(request.getMethod());
		String path = request.getRequestURI();
		ApiMenuPermissionRule rule = resolveRule(method, path);
		if (rule == null) {
			filterChain.doFilter(request, response);
			return;
		}

		Set<String> explicit = menuPermissionService.pathCodesFor(authentication);
		boolean ok = switch (rule.matchMode()) {
			case EXACT -> menuPermissionService.hasApiExactAccess(explicit, rule.pathCode());
			case SCREEN -> menuPermissionService.hasApiScreenAccess(explicit, rule.pathCode(), method);
		};
		if (!ok) {
			throw new AccessDeniedException("Accès refusé pour " + method + " " + path + " (requis: " + rule.pathCode() + ")");
		}
		filterChain.doFilter(request, response);
	}

	private static ApiMenuPermissionRule resolveRule(HttpMethod method, String path) {
		for (ApiMenuPermissionRule rule : ApiMenuPermissionRegistry.rules()) {
			if (rule.matches(method, path)) {
				return rule;
			}
		}
		return null;
	}
}
