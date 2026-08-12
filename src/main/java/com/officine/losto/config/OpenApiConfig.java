package com.officine.losto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

	private static final Logger log = LoggerFactory.getLogger(OpenApiConfig.class);

	@Value("${server.port:8080}")
	private int serverPort;

	/**
	 * Aide au diagnostic : si l’URL contient {@code springdoc-openapi-starter-common-2.5},
	 * le classpath d’exécution n’est pas celui de {@code mvn package} (IDE, ancien JAR, mauvais module).
	 */
	@Bean
	ApplicationRunner logSpringDocJarLocation() {
		return args -> {
			try {
				Class<?> c = Class.forName("org.springdoc.core.service.GenericResponseService");
				var source = c.getProtectionDomain().getCodeSource();
				String loc = source != null && source.getLocation() != null ? source.getLocation().toString() : "?";
				log.info("springdoc (GenericResponseService) chargé depuis: {}", loc);
			} catch (Throwable t) {
				log.warn("Impossible de localiser springdoc: {}", t.getMessage());
			}
		};
	}

	@Bean
	public OpenAPI officineOpenAPI() {
		final String bearerScheme = "bearerAuth";
		return new OpenAPI()
				.info(new Info()
						.title("Officine API")
						.description("API REST de gestion d’officine (Spring Boot). Documentation OpenAPI 3.")
						.version("1.0.0")
						.license(new License().name("Proprietary").url("https://example.com")))
				.addSecurityItem(new SecurityRequirement().addList(bearerScheme))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(bearerScheme,
						new SecurityScheme()
								.name(bearerScheme)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.description("JWT obtenu via POST /api/auth/login")))
				.servers(List.of(new Server()
						.url("http://localhost:" + serverPort)
						.description("Instance locale")));
	}
}
