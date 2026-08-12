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
public class ResultatMargeProduitLot {
	private Long pointDeVenteId;
	private Long productId;
	private Long batchId;
	private Long quantiteVendue;
	private BigDecimal chiffreAffaires;
	private BigDecimal coutRevient;
	private BigDecimal marge;
	private BigDecimal tauxMarge;
}
