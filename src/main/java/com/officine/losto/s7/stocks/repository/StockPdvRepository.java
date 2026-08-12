package com.officine.losto.s7.stocks.repository;

import com.officine.losto.entity.StockPdv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockPdvRepository extends JpaRepository<StockPdv, Long> {

	Optional<StockPdv> findByPointDeVente_IdAndProduct_Id(Long pointDeVenteId, Long productId);

	List<StockPdv> findByPointDeVente_Id(Long pointDeVenteId);

	@Query("""
			select sp from StockPdv sp join sp.pointDeVente pdv
			where (:siteId is null or pdv.site.id = :siteId)
			""")
	List<StockPdv> findForDashboard(@Param("siteId") Long siteId);

	@Query("""
			select coalesce(sum(sp.qteDisponible), 0)
			from StockPdv sp join sp.pointDeVente pdv
			where sp.product.id = :productId
				and (:siteId is null or pdv.site.id = :siteId)
			""")
	Integer sumQteDisponibleByProductAndSite(
			@Param("productId") Long productId, @Param("siteId") Long siteId);
}
