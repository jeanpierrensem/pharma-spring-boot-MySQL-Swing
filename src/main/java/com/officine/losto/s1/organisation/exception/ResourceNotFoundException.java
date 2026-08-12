package com.officine.losto.s1.organisation.exception;

import lombok.Getter;

/**
 * Ressource métier introuvable (404).
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

	private final String resourceType;
	private final Object resourceId;

	public ResourceNotFoundException(String resourceType, Object resourceId) {
		super(resourceType + " introuvable : id=" + resourceId);
		this.resourceType = resourceType;
		this.resourceId = resourceId;
	}

	public ResourceNotFoundException(String message) {
		super(message);
		this.resourceType = null;
		this.resourceId = null;
	}
}
