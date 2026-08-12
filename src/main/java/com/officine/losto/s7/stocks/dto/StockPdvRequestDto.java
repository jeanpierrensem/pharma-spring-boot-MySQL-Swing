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
public class StockPdvRequestDto {

	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private Long id;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long pointDeVenteId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long productId;

	private Integer qteDisponible;
	private Integer qteReservee;
	private Integer qteSeuilAlerte;

	private Long batchId;
	private BigDecimal costPrice;
	private BigDecimal sellPrice;
}
