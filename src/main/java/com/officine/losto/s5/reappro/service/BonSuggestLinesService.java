package com.officine.losto.s5.reappro.service;

import com.officine.losto.entity.Product;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.entity.Threshold;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.ThresholdRepo;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s5.reappro.dto.SuggestBonLineResponseDto;
import com.officine.losto.s7.stocks.domain.StockAlertSupport;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BonSuggestLinesService {

	private final StockPdvRepository stockPdvRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final ThresholdRepo thresholdRepo;
	private final ProductRepo productRepo;

	/**
	 * Produits du PDV dont le stock disponible est strictement inférieur au seuil alerte,
	 * avec quantité suggérée = max(0, seuil − disponible).
	 */
	@Transactional(readOnly = true)
	public List<SuggestBonLineResponseDto> suggestLinesForPointDeVente(long pointDeVenteId) {
		if (!pointDeVenteRepository.existsById(pointDeVenteId)) {
			throw new ResourceNotFoundException("PointDeVente", pointDeVenteId);
		}

		Integer defaultStockBasLevel = resolveDefaultStockBasLevel();
		List<SuggestBonLineResponseDto> out = new ArrayList<>();

		for (StockPdv row : stockPdvRepository.findByPointDeVente_Id(pointDeVenteId)) {
			if (row.getProduct() == null || row.getProduct().getId() == null) {
				continue;
			}
			int dispo = StockAlertSupport.nz(row.getQteDisponible());
			Integer effectiveSeuil =
					StockAlertSupport.effectiveSeuilAlerte(row.getQteSeuilAlerte(), defaultStockBasLevel);
			if (effectiveSeuil == null || !StockAlertSupport.isBelowAlert(dispo, effectiveSeuil)) {
				continue;
			}

			Product product =
					productRepo.findById(row.getProduct().getId()).orElse(null);
			if (product == null) {
				continue;
			}

			out.add(
					SuggestBonLineResponseDto.builder()
							.productId(product.getId())
							.productLabel(productLabel(product))
							.qteDisponible(dispo)
							.qteSeuilAlerte(effectiveSeuil)
							.quantity(
									StockAlertSupport.defaultOrderQuantity(
											dispo, row.getQteSeuilAlerte(), defaultStockBasLevel))
							.build());
		}

		out.sort(
				Comparator.comparing(
						SuggestBonLineResponseDto::getProductLabel,
						Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
		return out;
	}

	private Integer resolveDefaultStockBasLevel() {
		Threshold thr = thresholdRepo.findByCode(StockAlertSupport.STOCK_BAS_CODE);
		return thr == null || thr.getLevel() == null ? null : thr.getLevel();
	}

	private static String productLabel(Product product) {
		if (product == null) {
			return "";
		}
		if (product.getName() != null && !product.getName().isBlank()) {
			return product.getName();
		}
		return product.getId() == null ? "" : String.valueOf(product.getId());
	}
}
