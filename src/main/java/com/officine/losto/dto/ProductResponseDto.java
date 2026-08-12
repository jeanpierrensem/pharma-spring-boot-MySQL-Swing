package com.officine.losto.dto;

import lombok.*;

import java.math.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private String codeBar;
    private String famille;
    private EntityRefDto site;
    private EntityRefDto form;
    private EntityRefDto drugType;
    private EntityRefDto category;
    private EntityRefDto section;
    private EntityRefDto packaging;
    private List<EntityRefDto> thresholds;
    private String dosage;
    private int version;
    /**
     * Somme des quantités disponibles (stock central) — affichage / vente.
     */
    private Integer stockQuantity;
    /**
     * Dernier prix d'achat connu (dernier mouvement).
     */
    private BigDecimal latestCostPrice;
    /**
     * Dernier prix de vente connu (dernier mouvement).
     */
    private BigDecimal latestSellPrice;
    /**
     * Lot du dernier mouvement (référence affichage).
     */
    private EntityRefDto latestBatch;
    /**
     * Relative to API root (e.g. {@code products/12/photo}); join with base URL {@code http://host:port/api/}.
     */
    private String profilePhotoUrl;
}
