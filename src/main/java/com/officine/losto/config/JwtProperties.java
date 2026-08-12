package com.officine.losto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "officine.jwt")
public record JwtProperties(
		String secret,
		long accessTokenExpirationMs,
		long refreshTokenExpirationMs,
		long rememberMeRefreshTokenExpirationMs,
		String issuer
) {
}
