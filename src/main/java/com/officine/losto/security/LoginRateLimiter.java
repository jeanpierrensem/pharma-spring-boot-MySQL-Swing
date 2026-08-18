package com.officine.losto.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * Limite le nombre de tentatives de connexion échouées par couple
 * {@code login|IP} sur une fenêtre glissante.
 *
 * <p>Basé sur un cache Caffeine à expiration automatique
 * ({@code expireAfterWrite}) plutôt qu'une simple map : les entrées sont
 * évincées d'elles-mêmes {@link #WINDOW} après la première tentative
 * échouée, sans purge manuelle ni tâche planifiée à maintenir, et la taille
 * de la map ne peut donc pas croître indéfiniment.
 */
@Component
public class LoginRateLimiter {

	private static final int MAX_ATTEMPTS = 2;
	private static final Duration WINDOW = Duration.ofSeconds(300);

	private final Cache<String, AtomicInteger> attempts = Caffeine.newBuilder()
			.expireAfterWrite(WINDOW)
			.build();

	public boolean isBlocked(String key) {
		AtomicInteger count = attempts.getIfPresent(key);
		return count != null && count.get() >= MAX_ATTEMPTS;
	}

	public void recordFailure(String key) {
		attempts.asMap()
				.computeIfAbsent(key, k -> new AtomicInteger())
				.incrementAndGet();
	}

	public void reset(String key) {
		attempts.invalidate(key);
	}
}
