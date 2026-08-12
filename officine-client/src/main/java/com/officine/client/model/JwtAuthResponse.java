package com.officine.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JwtAuthResponse(
		String accessToken,
		String refreshToken,
		String tokenType,
		String username,
		List<String> roles,
		Instant expiration
) {
}
