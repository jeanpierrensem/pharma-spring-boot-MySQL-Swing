package com.officine.losto.backend.entity;

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
public final class CommandeLigne extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Commande commandeLigneCommande;
	@ManyToOne
	private Article commandeLigneArticle;
	private String commandeLigneReferenceArticle;
	private int commandeLigneQuantite;
	private int commandeLignePrixUnitaireHT;
	private int commandeLigneRemise;
	private int commandeLignePrixTotalHT;
	private String commandeLigneArticleName; 

	
	
	

}
