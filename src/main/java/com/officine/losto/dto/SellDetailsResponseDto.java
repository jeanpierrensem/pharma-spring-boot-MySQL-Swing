package com.officine.losto.dto;

import lombok.*;

import java.math.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsResponseDto {
    private Long id;
    private EntityRefDto product;
    private EntityRefDto batch;
    private int quantity;
    private int discount;
    private BigDecimal price;
    private BigDecimal unitCostAtSale;
}
