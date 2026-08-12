package com.officine.losto.s7.stocks.mapper;

import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.s7.stocks.dto.MouvementStockResponseDto;
import com.officine.losto.s7.stocks.dto.StockCentralResponseDto;
import com.officine.losto.s7.stocks.dto.StockPdvResponseDto;
import com.officine.losto.s7.stocks.util.StockPricingSupport;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StocksMapper {

	public StockCentralResponseDto toStockCentralResponse(StockCentral s) {
		if (s == null) {
			return null;
		}
		BigDecimal cost = s.getCostPrice();
		BigDecimal sell = s.getSellPrice();
		return StockCentralResponseDto.builder()
				.id(s.getId())
				.siteId(s.getSite() != null ? s.getSite().getId() : null)
				.magasinCentralId(s.getMagasinCentral() != null ? s.getMagasinCentral().getId() : null)
				.productId(s.getProduct() != null ? s.getProduct().getId() : null)
				.batchId(s.getBatch() != null ? s.getBatch().getId() : null)
				.batchNumber(s.getBatch() != null ? s.getBatch().getNumber() : null)
				.qteDisponible(s.getQteDisponible())
				.costPrice(cost)
				.sellPrice(sell)
				.margin(StockPricingSupport.margin(cost, sell))
				.qteReservee(s.getQteReservee())
				.qteSeuilAlerte(s.getQteSeuilAlerte())
				.updatedAt(s.getUpdatedAt())
				.build();
	}

	public StockPdvResponseDto toStockPdvResponse(StockPdv s) {
		if (s == null) {
			return null;
		}
		return StockPdvResponseDto.builder()
				.id(s.getId())
				.pointDeVenteId(s.getPointDeVente() != null ? s.getPointDeVente().getId() : null)
				.productId(s.getProduct() != null ? s.getProduct().getId() : null)
				.qteDisponible(s.getQteDisponible())
				.qteReservee(s.getQteReservee())
				.qteSeuilAlerte(s.getQteSeuilAlerte())
				.updatedAt(s.getUpdatedAt())
				.build();
	}

	public MouvementStockResponseDto toMouvementResponse(MouvementStock m) {
		if (m == null) {
			return null;
		}
		BigDecimal cost = m.getCostPrice();
		BigDecimal sell = m.getSellPrice();
		return MouvementStockResponseDto.builder()
				.id(m.getId())
				.productId(m.getProduct() != null ? m.getProduct().getId() : null)
				.batchId(m.getBatch() != null ? m.getBatch().getId() : null)
				.batchLabel(m.getBatch() != null ? m.getBatch().getNumber() : null)
				.typeMouvement(m.getTypeMouvement())
				.quantiteAlgebrique(m.getQuantiteAlgebrique())
				.costPrice(cost)
				.sellPrice(sell)
				.margin(StockPricingSupport.margin(cost, sell))
				.referenceType(m.getReferenceType())
				.referenceId(m.getReferenceId())
				.siteId(m.getSite() != null ? m.getSite().getId() : null)
				.pointDeVenteId(m.getPointDeVente() != null ? m.getPointDeVente().getId() : null)
				.userId(m.getAppUser() != null ? m.getAppUser().getId() : null)
				.dateMouvement(m.getDateMouvement())
				.commentaire(m.getCommentaire())
				.build();
	}
}
