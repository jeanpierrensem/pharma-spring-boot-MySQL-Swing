package com.officine.losto.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.officine.losto.s7.dashboard.DashboardService;
import com.officine.losto.s7.dashboard.dto.DashboardSyntheseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/dashboard", produces = "application/json")
@Tag(name = "Tableau de bord", description = "Synthèse activité PDV et magasin central")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/synthese")
	@Operation(summary = "Synthèse tableau de bord (CA, tickets, bons MC, alertes stock)")
	public DashboardSyntheseDto synthese(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtDebut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFin,
			@RequestParam(required = false) Long siteId) {
		return dashboardService.synthese(dtDebut, dtFin, siteId);
	}
}
