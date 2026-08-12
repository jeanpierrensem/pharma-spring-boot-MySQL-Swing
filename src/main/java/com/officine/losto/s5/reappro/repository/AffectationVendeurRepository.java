package com.officine.losto.s5.reappro.repository;

import com.officine.losto.entity.AffectationVendeur;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AffectationVendeurRepository
		extends JpaRepository<AffectationVendeur, Long>, JpaSpecificationExecutor<AffectationVendeur> {

	@EntityGraph(attributePaths = { "appUser", "pointDeVente" })
	@NonNull
	@Override
	Optional<AffectationVendeur> findById(@NonNull Long id);

	@EntityGraph(attributePaths = { "appUser", "pointDeVente" })
	@NonNull
	@Override
	List<AffectationVendeur> findAll();

	@EntityGraph(attributePaths = { "appUser", "pointDeVente" })
	@NonNull
	@Override
	List<AffectationVendeur> findAll(Specification<AffectationVendeur> spec);

	/** Chevauchement sur le même PDV et le même utilisateur (intervalles [debut, fin)). */
	@Query("""
			SELECT COUNT(a) FROM AffectationVendeur a
			WHERE a.pointDeVente.id = :pdvId AND a.appUser.id = :userId
			AND (:excludeId IS NULL OR a.id <> :excludeId)
			AND a.debut < :end AND a.fin > :start
			""")
	long countOverlappingSameSeller(
			@Param("pdvId") Long pdvId,
			@Param("userId") Long userId,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end,
			@Param("excludeId") Long excludeId);

	@EntityGraph(attributePaths = { "appUser", "pointDeVente" })
	@Query("""
			SELECT a FROM AffectationVendeur a
			WHERE a.pointDeVente.id = :pdvId
			AND a.debut < :rangeEndExclusive AND a.fin > :rangeStartInclusive
			ORDER BY a.debut ASC
			""")
	List<AffectationVendeur> findOverlappingRange(
			@Param("pdvId") Long pdvId,
			@Param("rangeStartInclusive") LocalDateTime rangeStartInclusive,
			@Param("rangeEndExclusive") LocalDateTime rangeEndExclusive);
}
