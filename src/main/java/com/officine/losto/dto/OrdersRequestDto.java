package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String number;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    private LocalDate orderDate;

    @Size(max = 2000)
    private String description;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Pattern(regexp = "NON|PARTIELLE|COMPLETE", message = "must be NON, PARTIELLE, or COMPLETE")
    @Size(max = 20)
    private String statut;

    @Positive
    private Long providerId;

    @Positive
    private Long userId;
}
