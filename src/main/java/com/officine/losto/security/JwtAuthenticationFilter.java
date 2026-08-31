package com.officine.losto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		String method = request.getMethod();
		String path = request.getRequestURI();
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String outcome;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			outcome = "pas d'en-tête Bearer (présent=" + (authHeader != null) + ")";
		} else {
			String jwt = authHeader.substring(7);
			try {
				String username = jwtService.extractUsername(jwt);
				if (username == null) {
					outcome = "username null dans le token";
				} else if (SecurityContextHolder.getContext().getAuthentication() != null) {
					outcome = "déjà authentifié en amont (ignoré)";
				} else {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (jwtService.isTokenValid(jwt, userDetails)) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
						outcome = "OK, authentifié en tant que " + username;
					} else {
						outcome = "token rejeté par isTokenValid (username=" + username + ")";
					}
				}
			} catch (RuntimeException e) {
				outcome = "exception " + e.getClass().getSimpleName() + ": " + e.getMessage();
				SecurityContextHolder.clearContext();
			}
		}
		log.warn("JwtAuthenticationFilter: IN {} {} — {}", method, path, outcome);
		filterChain.doFilter(request, response);
		log.warn("JwtAuthenticationFilter: OUT {} {} — status={}", method, path, response.getStatus());
	}
}
