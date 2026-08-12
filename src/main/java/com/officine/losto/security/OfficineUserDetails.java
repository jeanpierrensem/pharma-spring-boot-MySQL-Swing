package com.officine.losto.security;

import com.officine.losto.entity.AppGroup;
import com.officine.losto.entity.AppUser;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public final class OfficineUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final AppUser user;
	private final Collection<? extends GrantedAuthority> authorities;

	public OfficineUserDetails(AppUser user, Collection<? extends GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}

	public AppUser getAppUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return Boolean.TRUE.equals(user.getEnabled());
	}

	public static List<GrantedAuthority> authoritiesFor(AppUser user) {
		return RoleAuthorityMapper.mapGroupToAuthorities(user.getGroup());
	}

	public static final class RoleAuthorityMapper {

		public static final String ROLE_ADMIN = "ROLE_ADMIN";
		public static final String ROLE_USER = "ROLE_USER";

		private RoleAuthorityMapper() {
		}

		public static List<GrantedAuthority> mapGroupToAuthorities(AppGroup group) {
			if (group == null || group.getName() == null) {
				return List.of(new SimpleGrantedAuthority(ROLE_USER));
			}
			String normalized = group.getName().trim().toUpperCase(Locale.ROOT);
			if (normalized.contains("ADMIN") || "ADMINISTRATORS".equals(normalized)) {
				return List.of(new SimpleGrantedAuthority(ROLE_ADMIN));
			}
			return List.of(new SimpleGrantedAuthority(ROLE_USER));
		}

		public static List<String> roleNamesFor(AppUser user) {
			return mapGroupToAuthorities(user.getGroup()).stream()
					.map(GrantedAuthority::getAuthority)
					.map(RoleAuthorityMapper::stripRolePrefix)
					.toList();
		}

		public static String stripRolePrefix(String authority) {
			if (authority != null && authority.startsWith("ROLE_")) {
				return authority.substring(5);
			}
			return authority;
		}
	}
}
