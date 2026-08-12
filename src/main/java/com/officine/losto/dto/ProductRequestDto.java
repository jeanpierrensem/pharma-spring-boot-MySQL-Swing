package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 500)
    private String name;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 255)
    private String codeBar;

    /**
     * Libellé « famille » (texte métier ; pas une FK).
     */
    @Size(max = 255)
    private String famille;

    @Positive
    private Long siteId;

    @Positive
    private Long formId;

    @Positive
    private Long drugTypeId;

    @Positive
    private Long categoryId;

    @Positive
    private Long sectionId;

    @Positive
    private Long packagingId;

    /**
     * Identifiants des seuils associés (table {@code product_threshold}).
     * Liste vide = aucun seuil ; {@code null} = ne pas modifier les associations existantes (mise à jour partielle).
     */
    private List<Long> thresholdIds;

    @Size(max = 255)
    private String dosage;

    @Min(0)
    private Integer version;
}
