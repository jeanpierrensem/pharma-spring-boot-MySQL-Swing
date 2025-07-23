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
public final class ReceptionLigne extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private int quantiteRecue;
	private int quantiteManquante;
	private String date;
	
	@ManyToOne
	private AppUser appUsrer;
	
	@ManyToOne
	private CommandeLigne commandeLigne;
	
	private String observation;

}
