package com.officine.losto.service;

import com.officine.losto.config.JwtProperties;
import com.officine.losto.dto.auth.CurrentUserResponseDto;
import com.officine.losto.dto.auth.JwtAuthResponseDto;
import com.officine.losto.dto.auth.LoginRequestDto;
import com.officine.losto.dto.auth.RefreshTokenRequestDto;
import com.officine.losto.dto.auth.RegisterRequestDto;
import com.officine.losto.dto.mapper.DtoMapper;
import com.officine.losto.entity.AppGroup;
import com.officine.losto.entity.AppUser;
import com.officine.losto.entity.AuthAuditLog;
import com.officine.losto.entity.RefreshToken;
import com.officine.losto.model.AuthAuditLogRepo;
import com.officine.losto.model.GroupRepo;
import com.officine.losto.model.RefreshTokenRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.security.JwtService;
import com.officine.losto.security.LoginRateLimiter;
import com.officine.losto.security.LoginNormalizer;
import com.officine.losto.security.OfficineUserDetails;
import com.officine.losto.security.menu.MenuPermissionService;
import java.time.Instant;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	private static final String DEFAULT_USER_GROUP = "USER";

	private final UserRepo userRepo;
	private final GroupRepo groupRepo;
	private final RefreshTokenRepo refreshTokenRepo;
	private final AuthAuditLogRepo authAuditLogRepo;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final JwtProperties jwtProperties;
	private final LoginRateLimiter loginRateLimiter;
	private final DtoMapper dtoMapper;
	private final MenuPermissionService menuPermissionService;

	public AuthServiceImpl(UserRepo userRepo, GroupRepo groupRepo, RefreshTokenRepo refreshTokenRepo,
			AuthAuditLogRepo authAuditLogRepo, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService, JwtProperties jwtProperties,
			LoginRateLimiter loginRateLimiter, DtoMapper dtoMapper, MenuPermissionService menuPermissionService) {
		this.userRepo = userRepo;
		this.groupRepo = groupRepo;
		this.refreshTokenRepo = refreshTokenRepo;
		this.authAuditLogRepo = authAuditLogRepo;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.jwtProperties = jwtProperties;
		this.loginRateLimiter = loginRateLimiter;
		this.dtoMapper = dtoMapper;
		this.menuPermissionService = menuPermissionService;
	}

	@Override
	@Transactional
	public JwtAuthResponseDto register(RegisterRequestDto request) {
		if (userRepo.existsByLoginNormalized(request.getUsername())) {
			throw new IllegalArgumentException("Ce login est déjà utilisé");
		}
		if (userRepo.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Cet email est déjà utilisé");
		}
		AppGroup userGroup = resolveDefaultUserGroup();
		AppUser user = AppUser.builder()
				.login(LoginNormalizer.normalize(request.getUsername()))
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.group(userGroup)
				.enabled(true)
				.compteActif(true)
				.createdAt(Instant.now())
				.build();
		AppUser saved = userRepo.save(user);
		audit("REGISTER", saved.getLogin(), null, "Inscription réussie");
		return buildAuthResponse(saved, false);
	}

	@Override
	@Transactional
	public JwtAuthResponseDto login(LoginRequestDto request, String clientIp) {
		String rateLimitKey = request.getUsername() + "|" + clientIp;
		if (loginRateLimiter.isBlocked(rateLimitKey)) {
			audit("LOGIN_FAILURE", request.getUsername(), clientIp, "Trop de tentatives");
			throw new IllegalArgumentException("Trop de tentatives de connexion. Réessayez dans quelques minutes.");
		}
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			OfficineUserDetails principal = (OfficineUserDetails) authentication.getPrincipal();
			AppUser user = principal.getAppUser();
			loginRateLimiter.reset(rateLimitKey);
			audit("LOGIN_SUCCESS", user.getLogin(), clientIp, "Connexion réussie");
			return buildAuthResponse(user, request.isRememberMe());
		} catch (BadCredentialsException | DisabledException ex) {
			loginRateLimiter.recordFailure(rateLimitKey);
			audit("LOGIN_FAILURE", request.getUsername(), clientIp, ex.getMessage());
			throw new BadCredentialsException("Login ou mot de passe incorrect");
		}
	}

	@Override
	@Transactional
	public JwtAuthResponseDto refresh(RefreshTokenRequestDto request) {
		RefreshToken stored = refreshTokenRepo.findByToken(request.getRefreshToken())
				.filter(token -> !token.isRevoked())
				.filter(token -> token.getExpiryDate().isAfter(Instant.now()))
				.orElseThrow(() -> new IllegalArgumentException("Refresh token invalide ou expiré"));
		AppUser user = stored.getUser();
		stored.setRevoked(true);
		refreshTokenRepo.save(stored);
		return buildAuthResponse(user, stored.isRememberMe());
	}

	@Override
	@Transactional
	public void logout(String refreshToken, String username) {
		if (refreshToken != null && !refreshToken.isBlank()) {
			refreshTokenRepo.findByToken(refreshToken).ifPresent(token -> {
				token.setRevoked(true);
				refreshTokenRepo.save(token);
			});
		}
		if (username != null) {
			userRepo.findOptionalByLogin(username).ifPresent(user -> refreshTokenRepo.revokeAllActiveForUser(user.getId()));
		}
		audit("LOGOUT", username, null, "Déconnexion");
	}

	@Override
	@Transactional(readOnly = true)
	public CurrentUserResponseDto currentUser(String login) {
		AppUser user = userRepo.findOptionalByLogin(LoginNormalizer.normalize(login))
				.orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
		CurrentUserResponseDto dto = dtoMapper.toCurrentUserResponse(user);
		dto.setMenuPathCodes(menuPermissionService.menuPathCodesForLogin(login));
		dto.setApiPathCodes(menuPermissionService.apiPathCodesForLogin(login));
		return dto;
	}

	private JwtAuthResponseDto buildAuthResponse(AppUser user, boolean rememberMe) {
		OfficineUserDetails userDetails = new OfficineUserDetails(user, OfficineUserDetails.authoritiesFor(user));
		var roles = OfficineUserDetails.RoleAuthorityMapper.roleNamesFor(user);
		String accessToken = jwtService.generateAccessToken(userDetails, roles);
		Instant accessExpiry = jwtService.extractExpiration(accessToken);
		RefreshToken refreshToken = createRefreshToken(user, rememberMe);
		return JwtAuthResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken.getToken())
				.tokenType("Bearer")
				.username(user.getLogin())
				.roles(roles)
				.expiration(accessExpiry)
				.build();
	}

	private RefreshToken createRefreshToken(AppUser user, boolean rememberMe) {
		long ttl = rememberMe
				? jwtProperties.rememberMeRefreshTokenExpirationMs()
				: jwtProperties.refreshTokenExpirationMs();
		RefreshToken token = RefreshToken.builder()
				.token(UUID.randomUUID().toString())
				.user(user)
				.expiryDate(Instant.now().plusMillis(ttl))
				.rememberMe(rememberMe)
				.revoked(false)
				.build();
		return refreshTokenRepo.save(token);
	}

	private AppGroup resolveDefaultUserGroup() {
		AppGroup existing = groupRepo.findGroupByName(DEFAULT_USER_GROUP);
		if (existing != null) {
			return existing;
		}
		AppGroup consultants = groupRepo.findGroupByName("Consultants");
		if (consultants != null) {
			return consultants;
		}
		return groupRepo.save(AppGroup.builder()
				.name(DEFAULT_USER_GROUP)
				.description("Utilisateur standard")
				.selected(false)
				.build());
	}



	private void audit(String action, String username, String ip, String details) {
		authAuditLogRepo.save(AuthAuditLog.builder()
				.action(action)
				.username(username)
				.ipAddress(ip)
				.details(details)
				.createdAt(Instant.now())
				.build());
	}
}
