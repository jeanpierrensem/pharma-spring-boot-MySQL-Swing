package com.officine.losto.config;

import com.officine.losto.entity.Product;
import com.officine.losto.model.ProductRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Produits utilisés pour les jeux de démo qui référencent des code-barres historiques —
 * lorsqu’ils n’existent plus (seed CSV), prend les trois premiers produits insérés (id croissant).
 */
public final class DevSeedProductPins {

	private static final String LEGACY_BAR_PARA = "3770012345678";
	private static final String LEGACY_BAR_IBU = "3770012345685";
	private static final String LEGACY_BAR_VIT = "3770099990001";

	private DevSeedProductPins() {
	}

	public record Trio(Product first, Product second, Product third) {
	}

	/**
	 * @return trois produits pour les données de démo ventes/pilotage, ou {@code null} si aucun produit
	 */
	public static Trio resolveDemoProducts(ProductRepo repo) {
		Product legacy1 = repo.findByCodeBar(LEGACY_BAR_PARA);
		Product legacy2 = repo.findByCodeBar(LEGACY_BAR_IBU);
		Product legacy3 = repo.findByCodeBar(LEGACY_BAR_VIT);
		if (legacy1 != null && legacy2 != null && legacy3 != null) {
			return new Trio(legacy1, legacy2, legacy3);
		}
		List<Product> top = repo.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id"))).getContent();
		if (top.isEmpty()) {
			return null;
		}
		Product a = top.get(0);
		Product b = top.size() > 1 ? top.get(1) : a;
		Product c = top.size() > 2 ? top.get(2) : a;
		return new Trio(a, b, c);
	}
}
