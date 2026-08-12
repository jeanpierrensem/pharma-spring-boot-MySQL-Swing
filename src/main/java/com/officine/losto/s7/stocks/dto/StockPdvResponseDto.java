package com.officine.losto.s7.stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPdvResponseDto {
	private Long id;
	private Long pointDeVenteId;
	private Long productId;
	private Integer qteDisponible;
	private Integer qteReservee;
	private Integer qteSeuilAlerte;
	private LocalDateTime updatedAt;
}
