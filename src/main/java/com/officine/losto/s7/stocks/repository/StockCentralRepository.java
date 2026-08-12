package com.officine.losto.s7.stocks.repository;

import com.officine.losto.entity.StockCentral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface StockCentralRepository extends JpaRepository<StockCentral, Long> {

	Optional<StockCentral> findByMagasinCentral_IdAndProduct_IdAndBatch_Id(
			Long magasinCentralId, Long productId, Long batchId);

	List<StockCentral> findByMagasinCentral_IdAndProduct_Id(Long magasinCentralId, Long productId);

	List<StockCentral> findByMagasinCentral_Id(Long magasinCentralId);

	List<StockCentral> findBySite_Id(Long siteId);

	boolean existsByMagasinCentral_IdAndProduct_IdAndBatch_Id(
			Long magasinCentralId, Long productId, Long batchId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Transactional
	@Query("DELETE FROM StockCentral s WHERE s.magasinCentral.id = :magasinCentralId")
	void deleteByMagasinCentral_Id(@Param("magasinCentralId") Long magasinCentralId);

	@Query("SELECT COALESCE(SUM(s.qteDisponible), 0) FROM StockCentral s WHERE s.product.id = :productId")
	int sumQteDisponibleByProductId(@Param("productId") long productId);
}
