package com.officine.losto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * {@code officine.jwt.secret} est surchargé ici avec une valeur fixe (256
 * bits) : {@code @TestPropertySource} a une précédence supérieure aux
 * variables d'environnement du système dans Spring. Sans cette surcharge, ce
 * test dépend silencieusement de la variable d'environnement
 * {@code OFFICINE_JWT_SECRET} de la machine qui l'exécute (IDE, terminal,
 * CI...) : si elle est absente, trop courte, ou différente d'un poste à
 * l'autre, le test devient non déterministe, voire échoue avec
 * {@code WeakKeyException} si une valeur trop courte traîne dans
 * l'environnement local.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "officine.jwt.secret=Zm9mZmljaW5lLWRldl9qd3Rfc2VjcmV0X2tleV8yNTZfYml0cw==")
class OfficineApplicationTests {

	@Test
	void contextLoads() {
	}
}
