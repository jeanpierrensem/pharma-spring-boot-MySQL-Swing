package com.officine.losto.s7.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKpisDto {
	private BigDecimal caTotal;
	private long nombreTickets;
	private BigDecimal margeBrute;
	private BigDecimal tauxMargePct;
	private int bonsEnAttenteMc;
	private int alertesStockPdv;
	private int alertesStockCentral;
	private BigDecimal evolutionCaPct;
	private BigDecimal evolutionTicketsPct;
	private List<DashboardSeriePointDto> sparklineCa;
}
