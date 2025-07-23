package com.officine.losto.backend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class VenteLigne extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Vente vente;

	@ManyToOne
	private Article article;
	private int quantite;
	private int remise;
	private BigDecimal prixTotal; 


}
