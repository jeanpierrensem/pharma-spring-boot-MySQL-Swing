package com.officine.losto.security;

import com.officine.losto.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtProperties jwtProperties;
	private final SecretKey signingKey;

	public JwtService(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(normalizeSecret(jwtProperties.secret())));
	}

	public String generateAccessToken(UserDetails userDetails, List<String> roles) {
		Instant now = Instant.now();
		Instant expiry = now.plusMillis(jwtProperties.accessTokenExpirationMs());
		return Jwts.builder()
				.subject(userDetails.getUsername())
				.issuer(jwtProperties.issuer())
				.issuedAt(Date.from(now))
				.expiration(Date.from(expiry))
				.claim("roles", roles)
				.claim("type", "access")
				.signWith(signingKey)
				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Instant extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration).toInstant();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).isBefore(Instant.now());
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return resolver.apply(claims);
	}

	private static String normalizeSecret(String secret) {
		if (secret == null || secret.isBlank()) {
			throw new IllegalStateException("officine.jwt.secret must be configured");
		}
		if (secret.matches("^[A-Za-z0-9+/=]+$") && secret.length() >= 32) {
			return secret;
		}
		return java.util.Base64.getEncoder().encodeToString(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	}
}
