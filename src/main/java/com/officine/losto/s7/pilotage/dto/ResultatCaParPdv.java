package com.officine.losto.s7.pilotage.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultatCaParPdv {
	private Long pointDeVenteId;
	private String libellePdv;
	private BigDecimal chiffreAffaires;
}
