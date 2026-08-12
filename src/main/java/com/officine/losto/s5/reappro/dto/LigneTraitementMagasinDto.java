package com.officine.losto.s5.reappro.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneTraitementMagasinDto {

	@NotNull
	@PositiveOrZero
	private Long lineId;

	@NotNull
	@PositiveOrZero
	private Integer quantityDelivered;

	@Valid
	private List<BatchLivraisonDto> batchAllocations;
}
