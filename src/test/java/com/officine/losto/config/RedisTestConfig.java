package com.officine.losto.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis est exclu de l'auto-configuration en profil {@code test} (voir
 * {@code application-test.properties}) : aucune instance réelle n'est
 * disponible pendant les tests, et on ne veut pas dépendre de Docker/d'un
 * serveur Redis externe pour faire tourner la suite de tests.
 *
 * <p>{@link com.officine.losto.security.LoginRateLimiter} dépend d'un
 * {@link StringRedisTemplate} par injection de constructeur : sans ce bean,
 * tout test chargeant le contexte complet ({@code @SpringBootTest}, par
 * exemple {@code OfficineApplicationTests}) échouerait avec un
 * {@code NoSuchBeanDefinitionException}. Ce bean factice (mock Mockito, ne
 * fait jamais réellement d'appel réseau) sert uniquement à satisfaire cette
 * dépendance pour que le contexte démarre.
 *
 * <p>Pour tester le comportement du rate limiter lui-même (blocage après N
 * échecs, etc.), ne pas piloter ce mock bas niveau : mocker directement
 * {@link com.officine.losto.security.LoginRateLimiter} avec
 * {@code @MockBean} dans les tests qui exercent {@code AuthService}/
 * {@code AuthenticationController}.
 */
@Configuration
@Profile("test")
public class RedisTestConfig {

	@Bean
	StringRedisTemplate stringRedisTemplate() {
		return mock(StringRedisTemplate.class);
	}
}
