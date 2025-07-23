package com.officine.losto.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.officine.losto.backend.entity.utilities.Statut;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public final class Commande extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	private String commandeNumero; //1
	private String commandeDate  ;//2
	private String commandeLivraisonMode; //3 
	private String commandeInstruction  ; //4
	
	@ManyToOne
	private Fournisseur commandeFournisseur; //5	
	@ManyToOne
	private AppUser  commandeUser ; //6
	
	@Enumerated(EnumType.STRING)
	private Statut commandeStatut ; // 7 statut de la commande
	


}
