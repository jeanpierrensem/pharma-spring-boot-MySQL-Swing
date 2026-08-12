package com.officine.losto.s7.pilotage.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.officine.losto.entity.Sell;

/** Requêtes agrégées ventes pour le pilotage (s7). */
public interface PilotageVenteQueryRepository extends Repository<Sell, Long> {

	@Query("""
			select pdv.id, pdv.libelle,
				sum(sd.price * sd.quantity * (100 - sd.discount) / 100)
			from Sell s join s.lignes sd left join s.pointDeVente pdv
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			group by pdv.id, pdv.libelle
			order by sum(sd.price * sd.quantity * (100 - sd.discount) / 100) desc
			""")
	List<Object[]> caParPointDeVente(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("siteId") Long siteId);

	/**
	 * CA ligne = prix unitaire × quantité × (1 − remise % /100). Coût : {@code unitCostAtSale} sinon
	 * {@code SellDetails.unitCostAtSale}, sinon 0 (marge = CA si aucun coût).
	 */
	@Query("""
			select pdv.id, p.id, sd.batch.id,
				sum(sd.quantity),
				sum(sd.price * sd.quantity * (100 - sd.discount) / 100),
				sum(sd.quantity * coalesce(sd.unitCostAtSale, cast(0 as bigdecimal)))
			from Sell s join s.lignes sd join sd.product p left join s.pointDeVente pdv
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			group by pdv.id, p.id, sd.batch.id
			""")
	List<Object[]> margesParProduitEtLot(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("siteId") Long siteId);

	@Query("""
			select p.id, p.name,
				sum(sd.price * sd.quantity * (100 - sd.discount) / 100),
				sum(sd.quantity)
			from Sell s join s.lignes sd join sd.product p
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			group by p.id, p.name
			""")
	List<Object[]> aggregateProduits(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("siteId") Long siteId);

	@Query("""
			select month(s.dateVente), sum(sd.price * sd.quantity * (100 - sd.discount) / 100)
			from Sell s join s.lignes sd
			where s.dateVente between :yearStart and :yearEnd
				and (:siteId is null or s.site.id = :siteId)
			group by month(s.dateVente)
			order by sum(sd.price * sd.quantity * (100 - sd.discount) / 100) desc
			""")
	List<Object[]> moisLesPlusActifs(@Param("yearStart") LocalDate yearStart,
			@Param("yearEnd") LocalDate yearEnd, @Param("siteId") Long siteId);

	@Query("""
			select year(s.dateVente), sum(sd.price * sd.quantity * (100 - sd.discount) / 100)
			from Sell s join s.lignes sd
			where s.dateVente between :from and :to
				and (:siteId is null or s.site.id = :siteId)
			group by year(s.dateVente)
			order by sum(sd.price * sd.quantity * (100 - sd.discount) / 100) desc
			""")
	List<Object[]> anneesLesPlusActives(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("siteId") Long siteId);
}
