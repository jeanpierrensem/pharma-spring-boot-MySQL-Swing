package com.officine.losto.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

/**
 * Payload for generating a printable product sheet (PDF). Photo is optional (Base64).
 * Champs additionnels envoyés par le client JavaFX (graphique, seuils, etc.) sont ignorés.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPrintRequestDto {
    private Long productId;
    private String codeBar;
    private String name;
    /**
     * Famille métier (code famille).
     */
    private String famille;
    private String formLabel;
    private String drugTypeLabel;
    private String categoryLabel;
    private String sectionLabel;
    private String batchLabel;
    private String packagingLabel;
    private String dosage;
    private String quantity;
    private String costPrice;
    private String sellPrice;
    /**
     * Raw Base64 or full {@code data:image/...;base64,...} string.
     */
    private String photoBase64;
}
