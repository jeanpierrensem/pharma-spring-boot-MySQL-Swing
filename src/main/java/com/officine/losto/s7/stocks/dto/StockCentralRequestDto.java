package com.officine.losto.s7.stocks.dto;

import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCentralRequestDto {

	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private Long id;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long magasinCentralId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long productId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long batchId;

	private Integer qteDisponible;
	private Integer qteReservee;
	private Integer qteSeuilAlerte;

	/** Lot et prix portés par le mouvement d'entrée initial (mise en stock). */
	private BigDecimal costPrice;
	private BigDecimal sellPrice;
}
