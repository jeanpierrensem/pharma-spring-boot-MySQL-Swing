package com.officine.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiErrorResponse(
		int status,
		String error,
		String message,
		List<FieldViolation> fieldErrors
) {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record FieldViolation(String field, String message, String rejected) {
	}
}
