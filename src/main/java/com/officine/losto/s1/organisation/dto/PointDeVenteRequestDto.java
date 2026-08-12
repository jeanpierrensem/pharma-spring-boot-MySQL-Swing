package com.officine.losto.s1.organisation.dto;

import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDeVenteRequestDto {

	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private Long id;

	@NotNull(groups = ValidationGroups.OnCreate.class)
	private Long siteId;

	@NotBlank(groups = { ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class })
	@Size(max = 64)
	private String code;

	@NotBlank(groups = { ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class })
	@Size(max = 255)
	private String libelle;

	@Size(max = 500)
	private String adresse;

	@Size(max = 64)
	private String phone;

	private Boolean actif;
}
