package com.officine.losto.config;

import com.officine.losto.entity.AppUser;
import com.officine.losto.model.RefreshTokenRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.security.LoginNormalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Corrige les logins en double (ex. {@code admin} + {@code Admin} sur MySQL) avant le seed dev.
 */
@Component
@Profile("dev")
@Order(0)
public class AppUserLoginMaintenanceRunner implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AppUserLoginMaintenanceRunner.class);

	private final UserRepo userRepo;
	private final RefreshTokenRepo refreshTokenRepo;

	public AppUserLoginMaintenanceRunner(UserRepo userRepo, RefreshTokenRepo refreshTokenRepo) {
		this.userRepo = userRepo;
		this.refreshTokenRepo = refreshTokenRepo;
	}

	@Override
	@Transactional
	public void run(String... args) {
		List<AppUser> all = userRepo.findAll();
		if (all.isEmpty()) {
			return;
		}

		Map<String, List<AppUser>> byNormalizedLogin = new LinkedHashMap<>();
		for (AppUser user : all) {
			String key = normalizedKey(user.getLogin());
			byNormalizedLogin.computeIfAbsent(key, k -> new ArrayList<>()).add(user);
		}

		for (List<AppUser> group : byNormalizedLogin.values()) {
			if (group.size() <= 1) {
				continue;
			}
			group.sort(Comparator.comparing(AppUser::getId));
			AppUser keep = group.get(0);
			for (int i = 1; i < group.size(); i++) {
				AppUser duplicate = group.get(i);
				log.warn("Suppression utilisateur en double login≈{} : id={} (conservation id={})",
						keep.getLogin(), duplicate.getId(), keep.getId());
				refreshTokenRepo.deleteAllByUserId(duplicate.getId());
				userRepo.delete(duplicate);
			}
		}

		for (AppUser user : userRepo.findAll()) {
			String normalized = LoginNormalizer.normalize(user.getLogin());
			if (normalized != null && !normalized.equals(user.getLogin())) {
				user.setLogin(normalized);
				userRepo.save(user);
			}
		}
	}

	private static String normalizedKey(String login) {
		String normalized = LoginNormalizer.normalize(login);
		return normalized == null ? "" : normalized;
	}
}
