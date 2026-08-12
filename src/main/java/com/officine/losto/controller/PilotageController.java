package com.officine.losto.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.officine.losto.s7.pilotage.PilotageProduitSort;
import com.officine.losto.s7.pilotage.PilotageVenteService;
import com.officine.losto.s7.pilotage.dto.ClassementProduit;
import com.officine.losto.s7.pilotage.dto.PeriodeFiltre;
import com.officine.losto.s7.pilotage.dto.ResultatAnneePic;
import com.officine.losto.s7.pilotage.dto.ResultatCaParPdv;
import com.officine.losto.s7.pilotage.dto.ResultatMargeProduitLot;
import com.officine.losto.s7.pilotage.dto.ResultatMoisPic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/pilotage", produces = "application/json")
@Tag(name = "s7 — pilotage", description = "Indicateurs agrégés à partir des ventes (CA, marges, classements)")
public class PilotageController {

	private final PilotageVenteService pilotageVenteService;

	public PilotageController(PilotageVenteService pilotageVenteService) {
		this.pilotageVenteService = pilotageVenteService;
	}

	@GetMapping("/ca-par-point-de-vente")
	@Operation(summary = "Chiffre d'affaires par point de vente sur une période")
	public List<ResultatCaParPdv> caParPointDeVente(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtDebut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFin,
			@RequestParam(required = false) Long siteId) {
		return pilotageVenteService.caParPointDeVente(new PeriodeFiltre(dtDebut, dtFin, siteId));
	}

	@GetMapping("/marges-produit-lot")
	@Operation(summary = "Marges agrégées par PDV, produit et lot (coût : unitCostAtSale ou costPrice produit)")
	public List<ResultatMargeProduitLot> margesParProduitEtLot(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtDebut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFin,
			@RequestParam(required = false) Long siteId) {
		return pilotageVenteService.margesParProduitEtLot(new PeriodeFiltre(dtDebut, dtFin, siteId));
	}

	@GetMapping("/top-produits")
	@Operation(summary = "Produits les plus vendus (CA ou quantité)")
	public List<ClassementProduit> topProduits(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtDebut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFin,
			@RequestParam(required = false) Long siteId,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "CA") PilotageProduitSort sort) {
		return pilotageVenteService.topProduitsParCa(new PeriodeFiltre(dtDebut, dtFin, siteId), limit, sort);
	}

	@GetMapping("/mois-plus-actifs")
	@Operation(summary = "Mois les plus actifs (CA) pour une année civile")
	public List<ResultatMoisPic> moisLesPlusActifs(@RequestParam int annee,
			@RequestParam(required = false) Long siteId) {
		return pilotageVenteService.moisLesPlusActifs(annee, siteId);
	}

	@GetMapping("/annees-plus-actives")
	@Operation(summary = "Années les plus actives (CA) sur une plage de dates")
	public List<ResultatAnneePic> anneesLesPlusActives(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtDebut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFin,
			@RequestParam(required = false) Long siteId) {
		return pilotageVenteService.anneesLesPlusActives(new PeriodeFiltre(dtDebut, dtFin, siteId));
	}
}
