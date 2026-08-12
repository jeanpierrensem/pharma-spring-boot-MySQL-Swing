package com.officine.losto.dto.auth;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDto {

	private String accessToken;
	private String refreshToken;
	private String tokenType;
	private String username;
	private List<String> roles;
	private Instant expiration;
}
