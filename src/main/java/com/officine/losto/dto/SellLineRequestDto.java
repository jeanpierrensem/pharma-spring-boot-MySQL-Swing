package com.officine.losto.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellLineRequestDto {
    private Long id;

    @NotNull
    @Positive
    private Long productId;

    @Min(1)
    private int quantity;

    @Min(0)
    private int discount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    /**
     * Lot sorti (optionnel).
     */
    private Long batchId;

    /**
     * Coût unitaire à la vente (optionnel).
     */
    private BigDecimal unitCostAtSale;
}
