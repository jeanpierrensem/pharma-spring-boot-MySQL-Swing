package com.officine.losto.s7.dashboard.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSyntheseDto {
	private Long siteId;
	private Long magasinCentralId;
	private String magasinCentralLibelle;
	private LocalDate dtDebut;
	private LocalDate dtFin;
	private DashboardKpisDto kpis;
	private List<com.officine.losto.s7.pilotage.dto.ResultatCaParPdv> caParPdv;
	private List<DashboardTopProduitDto> topProduits;
	private List<DashboardBonResumeDto> bonsEnCours;
	private List<DashboardAlerteDto> alertes;
	private List<DashboardVenteRecenteDto> dernieresVentes;
	private List<DashboardMouvementDto> mouvementsRecents;
	private List<DashboardSeriePointDto> bonsTraitesParJour;
	private List<DashboardAlerteDto> rupturesPdv;
	private List<DashboardBonResumeDto> bonsEnRetard;
}
