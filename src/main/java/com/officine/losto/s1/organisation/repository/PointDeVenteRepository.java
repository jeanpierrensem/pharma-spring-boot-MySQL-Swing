package com.officine.losto.s1.organisation.repository;

import com.officine.losto.entity.PointDeVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointDeVenteRepository extends JpaRepository<PointDeVente, Long> {

	List<PointDeVente> findBySite_Id(Long siteId);

	Optional<PointDeVente> findByCode(String code);

	boolean existsByCodeAndIdNot(String code, Long id);

	boolean existsByCode(String code);
}
