package com.officine.losto.s7.dashboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.StatutBonCommandeInterne;

public interface DashboardBonQueryRepository extends Repository<BonCommandeInterne, Long> {

	@Query("""
			select b.orderDate, count(b)
			from BonCommandeInterne b
			where b.statut = :statut
				and b.orderDate between :from and :to
				and (:siteId is null or b.site.id = :siteId)
			group by b.orderDate
			order by b.orderDate
			""")
	List<Object[]> bonsParJour(
			@Param("from") LocalDate from,
			@Param("to") LocalDate to,
			@Param("siteId") Long siteId,
			@Param("statut") StatutBonCommandeInterne statut);
}
