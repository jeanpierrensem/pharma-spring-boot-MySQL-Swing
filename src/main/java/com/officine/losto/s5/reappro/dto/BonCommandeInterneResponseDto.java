package com.officine.losto.s5.reappro.dto;

import com.officine.losto.dto.EntityRefDto;
import com.officine.losto.entity.StatutBonCommandeInterne;
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
public class BonCommandeInterneResponseDto {
	private Long id;
	private String number;
	private LocalDate orderDate;
	private StatutBonCommandeInterne statut;
	private String statutLibelle;
	private String commentaire;
	private EntityRefDto site;
	private EntityRefDto pointDeVente;
	private EntityRefDto user;
	private EntityRefDto magasinCentral;
	private List<LigneBonCommandeInterneResponseDto> lines;
}
