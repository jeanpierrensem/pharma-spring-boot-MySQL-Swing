package com.officine.losto.s5.reappro.repository;

import com.officine.losto.entity.BonCommandeInterne;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface BonCommandeInterneRepository
		extends JpaRepository<BonCommandeInterne, Long>, JpaSpecificationExecutor<BonCommandeInterne> {

	boolean existsByNumber(String number);

	@EntityGraph(
			attributePaths = { "lignes", "lignes.product", "site", "pointDeVente", "user", "magasinCentral" })
	@NonNull
	@Override
	Optional<BonCommandeInterne> findById(@NonNull Long id);

	@EntityGraph(
			attributePaths = { "lignes", "lignes.product", "site", "pointDeVente", "user", "magasinCentral" })
	@NonNull
	@Override
	List<BonCommandeInterne> findAll();

	@EntityGraph(
			attributePaths = { "lignes", "lignes.product", "site", "pointDeVente", "user", "magasinCentral" })
	@NonNull
	@Override
	List<BonCommandeInterne> findAll(Specification<BonCommandeInterne> spec);
}
