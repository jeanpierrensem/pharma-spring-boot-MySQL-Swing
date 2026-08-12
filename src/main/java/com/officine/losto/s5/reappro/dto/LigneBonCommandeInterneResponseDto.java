package com.officine.losto.s5.reappro.dto;

import com.officine.losto.dto.EntityRefDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneBonCommandeInterneResponseDto {
	private Long id;
	private Integer quantity;
	private Integer quantityDelivered;
	private BigDecimal unitPrice;
	private EntityRefDto product;
	private EntityRefDto batch;
}
