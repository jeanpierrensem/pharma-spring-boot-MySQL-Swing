package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Categorie;


public interface CategorieRepository extends JpaRepository<Categorie, Long> {
	Categorie findByCategorieName(String categorieName);
	Optional<Categorie> findById(Long id);

	List<Categorie> findByCategorieDescriptionContainingOrCategorieNameContaining(String categorieDescription,
			String categorieName);

}
