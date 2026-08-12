package com.officine.losto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "REFRESH_TOKEN")
public final class RefreshToken extends AbstractEntity {

	@Column(name = "TOKEN", nullable = false, unique = true, length = 512)
	private String token;

	@Column(name = "EXPIRY_DATE", nullable = false)
	private Instant expiryDate;

	@Column(name = "REVOKED", nullable = false)
	@lombok.Builder.Default
	private boolean revoked = false;

	@Column(name = "REMEMBER_ME", nullable = false)
	@lombok.Builder.Default
	private boolean rememberMe = false;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID", nullable = false)
	private AppUser user;
}
