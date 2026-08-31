package com.officine.losto.security;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Limite le nombre de tentatives de connexion échouées par couple
 * {@code login|IP} sur une fenêtre fixe de 5 minutes.
 *
 * <p>État partagé via Redis ({@code INCR} + {@code EXPIRE}) plutôt qu'en
 * mémoire locale : ce backend tourne sur plusieurs instances derrière un
 * load balancer, toutes connectées à la même base centrale — un compteur
 * purement local par instance permettrait à un attaquant de multiplier la
 * limite effective en répartissant ses tentatives entre instances.
 *
 * <p>L'expiration n'est posée qu'au tout premier échec de la fenêtre (quand
 * le compteur passe à 1), pas à chaque tentative : la fenêtre reste fixe
 * (comme l'implémentation Caffeine précédente) plutôt que glissante.
 *
 * <p><b>Politique fail-open</b> : si Redis est injoignable, on laisse passer
 * la tentative de connexion plutôt que de bloquer tous les logins à cause
 * d'un incident d'infrastructure sans rapport avec l'authentification —
 * chaque échec Redis est journalisé en warning pour rester visible.
 */
@Slf4j
@Component
public class LoginRateLimiter {

	private static final int MAX_ATTEMPTS = 2;
	private static final Duration WINDOW = Duration.ofSeconds(300);
	private static final String KEY_PREFIX = "officine:login-rate-limit:";

	private final StringRedisTemplate redisTemplate;

	public LoginRateLimiter(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public boolean isBlocked(String key) {
		try {
			String value = redisTemplate.opsForValue().get(redisKey(key));
			return value != null && Long.parseLong(value) >= MAX_ATTEMPTS;
		} catch (DataAccessException ex) {
			log.warn("Redis indisponible, rate limiting de connexion désactivé temporairement (fail-open) : {}",
					ex.getMessage());
			return false;
		}
	}

	public void recordFailure(String key) {
		try {
			String redisKey = redisKey(key);
			Long count = redisTemplate.opsForValue().increment(redisKey);
			if (count != null && count == 1L) {
				redisTemplate.expire(redisKey, WINDOW);
			}
		} catch (DataAccessException ex) {
			log.warn("Redis indisponible, échec de connexion non comptabilisé (fail-open) : {}", ex.getMessage());
		}
	}

	public void reset(String key) {
		try {
			redisTemplate.delete(redisKey(key));
		} catch (DataAccessException ex) {
			log.warn("Redis indisponible, impossible de réinitialiser le compteur de tentatives : {}",
					ex.getMessage());
		}
	}

	private static String redisKey(String key) {
		return KEY_PREFIX + key;
	}
}
