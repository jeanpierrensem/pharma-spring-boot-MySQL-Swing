package com.officine.losto.exception;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private int status;
    private String error;
    private String message;
    private List<FieldViolation> fieldErrors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldViolation {
        private String field;
        private String message;
        /**
         * Valeur rejetée (texte) — évite {@code Object} pour la génération OpenAPI / springdoc.
         */
        private String rejected;
    }
}
