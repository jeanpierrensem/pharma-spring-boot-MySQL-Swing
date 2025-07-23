package com.officine.losto.backend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
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
public final class Article extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	private String articleName;
	private String articleDescription;
	private String articleCodeBarre; 

	@ManyToOne
	private Forme articleForme; 
	
	@ManyToOne
	private Typpe  articleTyppe ; //dci
	
	@ManyToOne
	private Categorie articleCategorie; 
	
	@ManyToOne
	private Rayon articleRayon ; 
	
	//@OneToOne(optional = true, cascade = CascadeType.ALL)
	@ManyToOne
	private Lot articleLot;  //peek datePeremption

	@ManyToOne //(optional = true, cascade = CascadeType.ALL)
	private Packaging  articlePackaging;  //peek datePeremption
	
	private String articleDosage; 
	private int articleQuantite_stock ; 
	private BigDecimal  articlePrixAchat; 
	private BigDecimal  articlePrixVente; 
	
	
	
	@Version
	@Column(name = "VERSION")
	private int version;
	
}
