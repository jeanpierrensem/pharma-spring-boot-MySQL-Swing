package com.officine.losto.s7.dashboard.dto;

import java.time.LocalDate;

import com.officine.losto.entity.StatutBonCommandeInterne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardBonResumeDto {
	private Long id;
	private String number;
	private LocalDate orderDate;
	private StatutBonCommandeInterne statut;
	private String statutLibelle;
	private Long pointDeVenteId;
	private String pointDeVenteLibelle;
	/** {@code true} si ENVOYE/PARTIEL depuis plus de 48 h (basé sur date commande). */
	private boolean enRetard;
	private long heuresEnAttente;
	/** {@code HAUTE}, {@code MOYENNE}, {@code BASSE}. */
	private String priorite;
}
