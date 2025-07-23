package com.officine.losto.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
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
public final class AppMenu extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	// @Column(name = "LIBELE ")
	//@NotBlank(message = "nom du role non valide")
	//private String groupecode;
	private String menuName;
	private String menuDescription;
}
