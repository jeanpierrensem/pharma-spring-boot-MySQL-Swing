package com.officine.losto.s7.stocks.util;

import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.Product;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;

import java.util.List;
import java.util.Optional;

public final class LatestProductMovementSupport {

	private LatestProductMovementSupport() {
	}

	public static Optional<MouvementStock> latest(MouvementStockRepository repo, Product product) {
		if (repo == null || product == null || product.getId() == null) {
			return Optional.empty();
		}
		List<MouvementStock> mvts = repo.findByProduct_IdOrderByDateMouvementDesc(product.getId());
		return mvts.isEmpty() ? Optional.empty() : Optional.of(mvts.get(0));
	}
}
