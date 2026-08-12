package com.officine.losto.s7.stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCentralResponseDto {
	private Long id;
	private Long siteId;
	private Long magasinCentralId;
	private Long productId;
	private Long batchId;
	private String batchNumber;
	private Integer qteDisponible;
	private BigDecimal costPrice;
	private BigDecimal sellPrice;
	private BigDecimal margin;
	private Integer qteReservee;
	private Integer qteSeuilAlerte;
	private LocalDateTime updatedAt;
}
