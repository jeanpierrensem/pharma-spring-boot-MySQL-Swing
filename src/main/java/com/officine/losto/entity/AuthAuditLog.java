package com.officine.losto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "AUTH_AUDIT_LOG")
public final class AuthAuditLog extends AbstractEntity {

	@Column(name = "USERNAME", length = 128)
	private String username;

	@Column(name = "ACTION", nullable = false, length = 64)
	private String action;

	@Column(name = "IP_ADDRESS", length = 64)
	private String ipAddress;

	@Column(name = "DETAILS", length = 512)
	private String details;

	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	private Instant createdAt;

	@PrePersist
	void onCreate() {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}
}
