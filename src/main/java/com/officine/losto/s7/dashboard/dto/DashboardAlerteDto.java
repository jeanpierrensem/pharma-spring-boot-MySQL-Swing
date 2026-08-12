package com.officine.losto.s7.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAlerteDto {
	/** {@code STOCK_PDV_BAS} ou {@code STOCK_CENTRAL_BAS}. */
	private String type;
	private String libelle;
	private String detail;
	private Long entityId;
	/** {@code CRITIQUE}, {@code AVERTISSEMENT}, {@code INFO}. */
	private String severite;
}
