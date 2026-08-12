package com.officine.losto.dto;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

/**
 * When {@code userIds} is null or omitted, the export includes all users.
 * When non-empty, only those users are included (order preserved).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserExportPdfRequest(List<Long> userIds) {
}
