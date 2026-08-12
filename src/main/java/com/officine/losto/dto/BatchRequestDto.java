package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String number;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    private LocalDate expiredDate;

    @Min(0)
    private int quantity;

    @Positive
    private Long providerId;
}
