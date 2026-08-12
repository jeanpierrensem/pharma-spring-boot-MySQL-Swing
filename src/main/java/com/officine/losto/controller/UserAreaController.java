package com.officine.losto.controller;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserAreaController {

	@GetMapping("/profile")
	public Map<String, Object> profile() {
		return Map.of(
				"message", "Zone utilisateur authentifié",
				"scope", "USER");
	}
}
