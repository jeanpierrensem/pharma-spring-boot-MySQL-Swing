package com.officine.losto.s7.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardVenteRecenteDto {
	private Long id;
	private String number;
	private String client;
	private BigDecimal totalPrice;
	private LocalDate dateVente;
	private String pointDeVenteLibelle;
	private String paymentStatus;
}
