package com.officine.losto.s1.organisation.repository;

import com.officine.losto.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {

	Optional<Site> findByCode(String code);

	boolean existsByCode(String code);

	boolean existsByCodeAndIdNot(String code, Long id);
}
