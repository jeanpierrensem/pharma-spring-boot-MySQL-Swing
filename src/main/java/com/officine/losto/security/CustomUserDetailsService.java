package com.officine.losto.security;

import com.officine.losto.entity.AppUser;
import com.officine.losto.model.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepo userRepo;

	public CustomUserDetailsService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = userRepo.findOptionalByLogin(LoginNormalizer.normalize(username))
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
		return new OfficineUserDetails(user, OfficineUserDetails.authoritiesFor(user));
	}
}
