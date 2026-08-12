package com.officine.losto.s1.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDeVenteResponseDto {
	private Long id;
	private Long siteId;
	private String code;
	private String libelle;
	private String adresse;
	private String phone;
	private boolean actif;
}
