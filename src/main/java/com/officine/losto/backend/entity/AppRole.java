package com.officine.losto.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//to resolve the infinite recursion problem in  bidirectional relationship
//use of @JsonIdentityInfo
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class AppRole extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private String rolename;
	private String roledescription;
	private String afficher ;
	private String ajouter;
	private String modifier;
	private String supprimer;
	private String imprimer;

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "GROUPE_ROLE", joinColumns = @JoinColumn(name = "app_groupe_id"), inverseJoinColumns = @JoinColumn(name = "app_role_id"))
	private List<AppGroupe> appGroupes = new ArrayList<AppGroupe>();

}
