package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailsRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @Min(0)
    private int receivedQuantity;

    @Min(0)
    private int missingQuantity;

    private LocalDateTime date;

    @Size(max = 2000)
    private String observation;

    @Positive
    private Long userId;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    @Positive
    private Long ordersDetailsId;
}
