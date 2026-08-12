package com.officine.losto.s5.reappro.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonTraitementMagasinRequestDto {

	@NotNull
	private Long bonId;

	@NotEmpty
	@Valid
	private List<LigneTraitementMagasinDto> lines;
}
