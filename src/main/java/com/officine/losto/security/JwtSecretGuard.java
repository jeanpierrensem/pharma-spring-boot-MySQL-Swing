package com.officine.losto.security;

import com.officine.losto.config.JwtProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Garde-fou de production : empêche le démarrage de l'application si
 * {@code officine.jwt.secret} est égal à la valeur par défaut utilisée en
 * développement (voir {@code application-dev.properties}).
 *
 * <p>Ce cas peut survenir même si {@code OFFICINE_JWT_SECRET} est bien
 * positionnée en production, par exemple à la suite d'un copier-coller
 * malheureux d'un fichier {@code .env} de dev. Comme cette valeur par défaut
 * est committée en clair dans le dépôt, l'utiliser en production revient à
 * publier le secret de signature des JWT.
 *
 * <p>Actif uniquement sur le profil {@code prod} : ne gêne ni le
 * développement local ni les tests.
 */
@Component
@Profile("prod")
public class JwtSecretGuard implements ApplicationRunner {

	/**
	 * Doit rester strictement identique à la valeur par défaut déclarée dans
	 * {@code application-dev.properties} (officine.jwt.secret).
	 */
	private static final String DEV_DEFAULT_SECRET =
			"Zm9mZmljaW5lLWRldl9qd3Rfc2VjcmV0X2tleV8yNTZfYml0cw==";

	private final JwtProperties jwtProperties;

	public JwtSecretGuard(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (DEV_DEFAULT_SECRET.equals(jwtProperties.secret())) {
			throw new IllegalStateException(
					"officine.jwt.secret est positionné sur la valeur par défaut de développement. "
							+ "Configurez une valeur dédiée et confidentielle pour la production via la variable "
							+ "d'environnement OFFICINE_JWT_SECRET.");
		}
	}
}
