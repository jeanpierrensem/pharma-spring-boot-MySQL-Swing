package com.officine.losto.backend.entity;

import java.io.Serializable;

import org.springframework.context.annotation.Description;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code AbstractEntity} class is the abstract class used to keep some common values used by most of the classes.
 * by designing this way, we avoid repeating in subclass id, version parameters, considering that all entities will
 * have at least and id and version
 *
 * <p> @MappedSuperclass : Designates a class whose mapping information is applied to the entities that inherit from it. A mapped
 * superclass has no separate table defined for it.
 * <p> this class did not provide any method
 *
 * @author  JP NSEM
 * @since   1.0
 */


@Description(value = "This bean is designed to be heritated by all bean and serve id and  version properties ")
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public sealed abstract class AbstractEntity implements Serializable
permits
AppUser,
AppMenu , 
AppGroupe,
Forme, 
Typpe, 
Categorie, 
Rayon,
Lot, 
Fournisseur, 
Seuil, 
Article, 
AppRole, 
Packaging, 
CommandeLigne, 
Commande, 
ReceptionLigne, 
Vente, 
VenteLigne

{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private  Long id;

}
