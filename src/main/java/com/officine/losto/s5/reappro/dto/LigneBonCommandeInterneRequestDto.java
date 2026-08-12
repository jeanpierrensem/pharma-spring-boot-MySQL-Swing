package com.officine.losto.s5.reappro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneBonCommandeInterneRequestDto {
	private Long id;

	@NotNull
	@Positive
	private Long productId;

	@NotNull
	@Positive
	private Integer quantity;

	/** Renseigné au magasin central (lot / prix) — optionnel côté PDV. */
	private BigDecimal unitPrice;

	/** Lot attribué au traitement au magasin central (obligatoire au passage ENVOYE → TRAITE). */
	private Long batchId;
}
