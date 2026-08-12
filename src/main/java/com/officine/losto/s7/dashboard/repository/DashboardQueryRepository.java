package com.officine.losto.s7.dashboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.officine.losto.entity.Sell;

/** Requêtes agrégées ventes pour le tableau de bord. */
public interface DashboardQueryRepository extends Repository<Sell, Long> {

	@Query("""
			select count(s)
			from Sell s
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			""")
	long countTickets(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("siteId") Long siteId);

	@Query("""
			select coalesce(sum(sd.price * sd.quantity * (100 - sd.discount) / 100), 0),
				coalesce(sum(sd.quantity * coalesce(sd.unitCostAtSale, 0)), 0)
			from Sell s join s.lignes sd
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			""")
	List<Object[]> caEtCout(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("siteId") Long siteId);

	@Query("""
			select s.dateVente,
				sum(sd.price * sd.quantity * (100 - sd.discount) / 100)
			from Sell s join s.lignes sd
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			group by s.dateVente
			order by s.dateVente
			""")
	List<Object[]> caParJour(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("siteId") Long siteId);

	@Query("""
			select s.id, s.number, s.client, s.totalPrice, s.dateVente, pdv.libelle, s.paymentStatus
			from Sell s left join s.pointDeVente pdv
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			order by s.dateVente desc, s.id desc
			""")
	List<Object[]> dernieresVentes(
			@Param("from") LocalDate from,
			@Param("to") LocalDate to,
			@Param("siteId") Long siteId,
			Pageable pageable);
}
