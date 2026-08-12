package com.officine.losto.controller;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@GetMapping("/overview")
	public Map<String, Object> overview() {
		return Map.of(
				"message", "Zone réservée aux administrateurs",
				"scope", "ADMIN");
	}
}
