package com.officine.losto.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//to resolve the infinite recursion problem in  bidirectional relationship
//use of @JsonIdentityInfo
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity(name = "AppUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class AppUser extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	//@Column(name = "LOGIN")
	//@NotBlank(message = "{User.login}")
	private String username;

	//@Column(name = "MOT_DE_PASSE")
	//@NotBlank(message = "{User.motDePasse}")
	private String password;

	//@Column(name = "MATRICULE")
	//@NotBlank(message = "{User.matricule}")
	private String matricule;

	//@Column(name = "NOM"
	private String nom;

	//@Column(name = "PRENOM")
	//@NotBlank(message = "{User.prenom}")
	//@Size(min = 10, max = 50, message = "Le prenom doit doit avoir entre 10 et 50")
	private String prenom;

	//@Column(name = "DATE_NAIDSSANCE")
	//@Temporal(TemporalType.DATE)
	//@PastOrPresent(message = "{User.dateNaissance}e")
	//private Date dateNaissance;

	//@NotBlank(message = "{User.lieuNaissance}")
	//@Column(name = "LIEU_NAISSANCE")
	//private String lieuNaissance;


	//@JsonBackReference
	//@JoinColumn(name = "GROUPE_ID")
	 @ManyToOne
	 private AppGroupe appGroupe;


	//@ManyToOne
	//@JsonBackReference
	//@JoinColumn(name = "AGENCE_ID") // foreignKey in User table
	//private Agence agence;

	//User can have many Groups and Group can belong to many user
	//to resolve the infinite recursion problem in bidirectional relationship
	//use of @JsonManagedReference
	//@JsonBackReference
	//@ManyToOne
	//@JoinColumn(name = "GROUPE_ID")
	//private Collection<AppGroupe> appGroupes;
}
