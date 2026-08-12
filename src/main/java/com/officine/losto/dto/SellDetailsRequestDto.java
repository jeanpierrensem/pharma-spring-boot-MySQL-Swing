package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    @Positive
    private Long sellId;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    @Positive
    private Long productId;

    @Min(1)
    private int quantity;

    @Min(0)
    private int discount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    private Long batchId;

    private BigDecimal unitCostAtSale;
}
