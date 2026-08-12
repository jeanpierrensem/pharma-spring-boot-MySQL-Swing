package com.officine.losto.s5.reappro.dto;

import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffectationVendeurRequestDto {
	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private Long id;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private LocalDateTime debut;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private LocalDateTime fin;

	private Boolean actifCreneau;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long appUserId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long pointDeVenteId;
}
