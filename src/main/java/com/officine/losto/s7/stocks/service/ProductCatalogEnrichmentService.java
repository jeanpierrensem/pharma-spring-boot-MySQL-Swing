package com.officine.losto.s7.stocks.service;

import com.officine.losto.dto.EntityRefDto;
import com.officine.losto.dto.ProductResponseDto;
import com.officine.losto.entity.Batch;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.Product;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCatalogEnrichmentService {

	private final StockCentralRepository stockCentralRepository;
	private final MouvementStockRepository mouvementStockRepository;

	@Transactional(readOnly = true)
	public ProductResponseDto enrich(ProductResponseDto base, Product product) {
		if (base == null || product == null || product.getId() == null) {
			return base;
		}
		long productId = product.getId();
		base.setStockQuantity(stockCentralRepository.sumQteDisponibleByProductId(productId));
		List<MouvementStock> mvts = mouvementStockRepository.findByProduct_IdOrderByDateMouvementDesc(productId);
		if (!mvts.isEmpty()) {
			MouvementStock latest = mvts.get(0);
			base.setLatestCostPrice(latest.getCostPrice());
			base.setLatestSellPrice(latest.getSellPrice());
			Batch b = latest.getBatch();
			if (b != null) {
				base.setLatestBatch(EntityRefDto.builder()
						.id(b.getId())
						.code(b.getNumber())
						.label(b.getNumber())
						.build());
			}
		}
		return base;
	}
}
