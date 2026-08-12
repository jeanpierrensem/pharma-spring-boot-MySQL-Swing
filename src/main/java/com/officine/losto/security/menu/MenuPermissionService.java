package com.officine.losto.security.menu;

import com.officine.losto.catalog.MenuImplicitPermissionCatalog;
import com.officine.losto.entity.AppGroup;
import com.officine.losto.entity.AppUser;
import com.officine.losto.entity.Menu;
import com.officine.losto.model.UserRepo;
import com.officine.losto.security.LoginNormalizer;
import com.officine.losto.security.OfficineUserDetails;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuPermissionService {

	private final UserRepo userRepo;

	public MenuPermissionService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Transactional(readOnly = true)
	public Set<String> pathCodesForLogin(String login) {
		if (login == null || login.isBlank()) {
			return Set.of();
		}
		AppUser user = userRepo.findWithGroupMenusByLoginNormalized(login).orElse(null);
		if (user == null) {
			return Set.of();
		}
		if (isAdminUser(user)) {
			return Set.of("*");
		}
		AppGroup group = user.getGroup();
		if (group == null || group.getMenus() == null) {
			return Set.of();
		}
		return group.getMenus().stream()
				.map(Menu::getPathCode)
				.filter(Objects::nonNull)
				.filter(p -> !p.isBlank())
				.collect(Collectors.toUnmodifiableSet());
	}

	@Transactional(readOnly = true)
	public Set<String> pathCodesFor(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return Set.of();
		}
		if (isAdminAuthentication(authentication)) {
			return Set.of("*");
		}
		return pathCodesForLogin(LoginNormalizer.normalize(authentication.getName()));
	}

	public boolean isAdmin(Authentication authentication) {
		return isAdminAuthentication(authentication);
	}

	public boolean hasExactPath(Set<String> allowed, String pathCode) {
		if (pathCode == null || pathCode.isBlank()) {
			return true;
		}
		if (allowed.contains("*")) {
			return true;
		}
		return allowed.contains(pathCode);
	}

	public boolean hasScreenAccess(Set<String> allowed, String screenPathCode) {
		if (screenPathCode == null || screenPathCode.isBlank()) {
			return true;
		}
		if (allowed.contains("*")) {
			return true;
		}
		for (String path : allowed) {
			if (path.equals(screenPathCode) || path.startsWith(screenPathCode + ".")) {
				return true;
			}
		}
		return false;
	}

	/** Garde API : écran explicite ou droit implicite en lecture (GET/HEAD uniquement). */
	public boolean hasApiScreenAccess(Set<String> explicitAllowed, String screenPathCode, HttpMethod method) {
		if (hasScreenAccess(explicitAllowed, screenPathCode)) {
			return true;
		}
		if (isReadMethod(method)) {
			return MenuImplicitPermissionCatalog.isImplicitReadGranted(explicitAllowed, screenPathCode);
		}
		return false;
	}

	/** Garde API mode EXACT : jamais de droit implicite (création / modification / suppression). */
	public boolean hasApiExactAccess(Set<String> explicitAllowed, String pathCode) {
		return hasExactPath(explicitAllowed, pathCode);
	}

	/** Habilitations explicites + droits implicites de lecture pour le client et la doc API. */
	public Set<String> effectiveApiPathCodes(Set<String> explicitAllowed) {
		return MenuImplicitPermissionCatalog.effectiveApiPathCodes(explicitAllowed);
	}

	public Set<String> effectiveApiPathCodesFor(Authentication authentication) {
		return effectiveApiPathCodes(pathCodesFor(authentication));
	}

	public List<String> apiPathCodesForLogin(String login) {
		return effectiveApiPathCodes(pathCodesForLogin(login)).stream()
				.filter(p -> !"*".equals(p))
				.sorted()
				.toList();
	}

	private static boolean isReadMethod(HttpMethod method) {
		return method == HttpMethod.GET || method == HttpMethod.HEAD;
	}

	public void requireExact(Authentication authentication, String pathCode) {
		Set<String> allowed = pathCodesFor(authentication);
		if (!hasExactPath(allowed, pathCode)) {
			throw new AccessDeniedException("Habilitation requise : " + pathCode);
		}
	}

	public void requireScreen(Authentication authentication, String screenPathCode) {
		Set<String> allowed = pathCodesFor(authentication);
		if (!hasScreenAccess(allowed, screenPathCode)) {
			throw new AccessDeniedException("Habilitation requise : " + screenPathCode);
		}
	}

	private static boolean isAdminAuthentication(Authentication authentication) {
		if (authentication == null) {
			return false;
		}
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (OfficineUserDetails.RoleAuthorityMapper.ROLE_ADMIN.equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAdminUser(AppUser user) {
		if (user == null || user.getGroup() == null || user.getGroup().getName() == null) {
			return false;
		}
		String normalized = user.getGroup().getName().trim().toUpperCase(Locale.ROOT);
		return normalized.contains("ADMIN") || "ADMINISTRATORS".equals(normalized);
	}

	public List<String> menuPathCodesForLogin(String login) {
		return pathCodesForLogin(login).stream()
				.filter(p -> !"*".equals(p))
				.sorted()
				.toList();
	}
}
