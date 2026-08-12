package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdResponseDto {
    private Long id;
    private String code;
    private int level;
    private String description;
    /**
     * Couleur associée au seuil (#RGB ou #RRGGBB).
     */
    private String colorHex;
}
