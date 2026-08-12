package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDetailsRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    @Positive
    private Long ordersId;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    @Positive
    private Long productId;

    @Min(0)
    private int quantity;

    @Min(0)
    private int unitPrice;

    @Min(0)
    private int discount;

    @Min(0)
    private int totalPrice;
}
