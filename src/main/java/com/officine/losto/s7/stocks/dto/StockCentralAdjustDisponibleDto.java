package com.officine.losto.s7.stocks.dto;

import com.officine.losto.s7.stocks.domain.ReferenceStockType;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCentralAdjustDisponibleDto {

	@NotNull
	private Long magasinCentralId;

	@NotNull
	private Long productId;

	@NotNull
	private Long batchId;

	/** Variation algébrique sur la quantité disponible (ex. +10 entrée, -3 sortie). */
	@NotNull
	private Integer delta;

	@NotNull
	private TypeMouvementStock typeMouvement;

	private ReferenceStockType referenceType;
	private Long referenceId;
	/** Optionnel ; par défaut : site du magasin central. */
	private Long siteId;
	private Long pointDeVenteId;
	private Long userId;
	private String commentaire;

	@DecimalMin(value = "0.0", inclusive = true)
	private BigDecimal costPrice;

	@DecimalMin(value = "0.0", inclusive = true)
	private BigDecimal sellPrice;
}
