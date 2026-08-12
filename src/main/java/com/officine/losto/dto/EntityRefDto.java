package com.officine.losto.dto;

import lombok.*;

/**
 * Stable reference to another aggregate (id + optional display fields). Used instead of exposing JPA entities in API graphs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityRefDto {
    private Long id;
    private String code;
    private String label;
    /**
     * Renseigné pour certains agrégats (ex. seuil : couleur d’affichage).
     */
    private String colorHex;
}
