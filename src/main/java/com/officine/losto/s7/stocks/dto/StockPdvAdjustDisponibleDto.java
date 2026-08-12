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
public class StockPdvAdjustDisponibleDto {

	@NotNull
	private Long pointDeVenteId;

	@NotNull
	private Long productId;

	@NotNull
	private Integer delta;

	@NotNull
	private TypeMouvementStock typeMouvement;

	private ReferenceStockType referenceType;
	private Long referenceId;
	private Long siteId;
	private Long userId;
	private String commentaire;

	@Positive
	private Long batchId;

	@DecimalMin(value = "0.0", inclusive = true)
	private BigDecimal costPrice;

	@DecimalMin(value = "0.0", inclusive = true)
	private BigDecimal sellPrice;
}
