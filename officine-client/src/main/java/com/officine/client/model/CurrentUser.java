package com.officine.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrentUser(
		Long id,
		String login,
		String name,
		String email,
		boolean enabled,
		Instant createdAt,
		List<String> roles
) {
}
