package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String code;

    @Min(0)
    private int level;

    @Size(max = 1000)
    private String description;

    /**
     * Couleur associée au seuil (#RGB ou #RRGGBB), optionnelle.
     */
    @Size(max = 32)
    private String colorHex;
}
