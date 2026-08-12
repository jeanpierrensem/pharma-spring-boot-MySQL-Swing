package com.officine.losto.s1.organisation.repository;

import com.officine.losto.entity.MagasinCentral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MagasinCentralRepository extends JpaRepository<MagasinCentral, Long> {

	Optional<MagasinCentral> findBySite_Id(Long siteId);

	boolean existsBySite_Id(Long siteId);

	boolean existsByCodeAndIdNot(String code, Long id);

	boolean existsByCode(String code);
}
