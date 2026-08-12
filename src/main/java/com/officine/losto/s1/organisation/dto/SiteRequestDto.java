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
public class SiteRequestDto {

	@NotNull(groups = ValidationGroups.OnUpdate.class)
	private Long id;

	@NotBlank(groups = { ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class })
	@Size(max = 64)
	private String code;

	@NotBlank(groups = { ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class })
	@Size(max = 255)
	private String libelle;

	private Long responsableUserId;

	private Boolean actif;
}
