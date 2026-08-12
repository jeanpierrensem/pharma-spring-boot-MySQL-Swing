package com.officine.losto.s7.stocks.dto;

import com.officine.losto.s7.stocks.domain.ReferenceStockType;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
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
public class MouvementStockResponseDto {
	private Long id;
	private Long productId;
	private Long batchId;
	private String batchLabel;
	private TypeMouvementStock typeMouvement;
	private Integer quantiteAlgebrique;
	private BigDecimal costPrice;
	private BigDecimal sellPrice;
	private BigDecimal margin;
	private ReferenceStockType referenceType;
	private Long referenceId;
	private Long siteId;
	private Long pointDeVenteId;
	private Long userId;
	private LocalDateTime dateMouvement;
	private String commentaire;
}
