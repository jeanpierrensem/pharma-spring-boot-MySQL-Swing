package com.officine.losto.s7.dashboard.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTopProduitDto {
	private int rang;
	private Long productId;
	private String libelle;
	private BigDecimal chiffreAffaires;
	private Long quantiteVendue;
	private Integer stockRestant;
}
