package com.officine.losto.s5.reappro.dto;

import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonCommandeInterneRequestDto {
	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private Long id;

	@Size(max = 64)
	private String number;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private LocalDate orderDate;

	private StatutBonCommandeInterne statut;

	@Size(max = 2000)
	private String commentaire;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long pointDeVenteId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long siteId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long userId;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long magasinCentralId;

	@Valid
	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private List<LigneBonCommandeInterneRequestDto> lines;
}
