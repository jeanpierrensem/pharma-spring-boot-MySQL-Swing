package com.officine.losto.s5.reappro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Ligne suggérée pour un brouillon de bon interne (produits PDV sous seuil alerte). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestBonLineResponseDto {
	private Long productId;
	private String productLabel;
	private Integer qteDisponible;
	private Integer qteSeuilAlerte;
	private Integer quantity;
}
