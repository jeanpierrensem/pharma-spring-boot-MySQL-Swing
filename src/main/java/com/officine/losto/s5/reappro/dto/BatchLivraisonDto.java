package com.officine.losto.s5.reappro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchLivraisonDto {

	@NotNull
	@Positive
	private Long batchId;

	@NotNull
	@Positive
	private Integer quantity;
}
