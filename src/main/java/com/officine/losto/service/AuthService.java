package com.officine.losto.service;

import com.officine.losto.dto.auth.CurrentUserResponseDto;
import com.officine.losto.dto.auth.JwtAuthResponseDto;
import com.officine.losto.dto.auth.LoginRequestDto;
import com.officine.losto.dto.auth.RefreshTokenRequestDto;
import com.officine.losto.dto.auth.RegisterRequestDto;

public interface AuthService {

	JwtAuthResponseDto register(RegisterRequestDto request);

	JwtAuthResponseDto login(LoginRequestDto request, String clientIp);

	JwtAuthResponseDto refresh(RefreshTokenRequestDto request);

	void logout(String refreshToken, String username);

	CurrentUserResponseDto currentUser(String login);
}
