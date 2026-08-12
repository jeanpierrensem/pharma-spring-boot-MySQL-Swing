package com.officine.losto.s1.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteResponseDto {
	private Long id;
	private String code;
	private String libelle;
	private Long responsableUserId;
	private boolean actif;
	private Long magasinCentralId;
}
