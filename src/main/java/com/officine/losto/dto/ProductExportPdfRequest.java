package com.officine.losto.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * When {@code productIds} is null or omitted, the export includes all products.
 * When non-empty, only those products are included (order preserved).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductExportPdfRequest(List<Long> productIds) {
}
