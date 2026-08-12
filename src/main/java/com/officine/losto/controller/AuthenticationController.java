package com.officine.losto.controller;

import com.officine.losto.dto.auth.JwtAuthResponseDto;
import com.officine.losto.dto.auth.LoginRequestDto;
import com.officine.losto.dto.auth.RefreshTokenRequestDto;
import com.officine.losto.dto.auth.RegisterRequestDto;
import com.officine.losto.security.OfficineUserDetails;
import com.officine.losto.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	private final AuthService authService;

	public AuthenticationController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<JwtAuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	public JwtAuthResponseDto login(@Valid @RequestBody LoginRequestDto request, HttpServletRequest httpRequest) {
		return authService.login(request, resolveClientIp(httpRequest));
	}

	@PostMapping("/refresh")
	public JwtAuthResponseDto refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
		return authService.refresh(request);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody(required = false) RefreshTokenRequestDto request,
			@AuthenticationPrincipal OfficineUserDetails principal) {
		String refreshToken = request != null ? request.getRefreshToken() : null;
		String username = principal != null ? principal.getUsername() : null;
		authService.logout(refreshToken, username);
		return ResponseEntity.noContent().build();
	}

	private static String resolveClientIp(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (forwarded != null && !forwarded.isBlank()) {
			return forwarded.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}
