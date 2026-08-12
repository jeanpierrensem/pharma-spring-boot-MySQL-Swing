package com.officine.losto.s7.stocks.repository;

import com.officine.losto.entity.MouvementStock;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

	@EntityGraph(attributePaths = "batch")
	@Override
	List<MouvementStock> findAll();

	@EntityGraph(attributePaths = "batch")
	List<MouvementStock> findByProduct_IdOrderByDateMouvementDesc(Long productId);

	@EntityGraph(attributePaths = "batch")
	List<MouvementStock> findBySite_IdOrderByDateMouvementDesc(Long siteId);

	@EntityGraph(attributePaths = "batch")
	List<MouvementStock> findByPointDeVente_IdOrderByDateMouvementDesc(Long pointDeVenteId);

	@EntityGraph(attributePaths = "batch")
	List<MouvementStock> findByBatch_Id(Long batchId);
}
