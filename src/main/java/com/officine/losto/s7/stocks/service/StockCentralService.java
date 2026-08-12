package com.officine.losto.s7.stocks.service;

import com.officine.losto.s7.stocks.dto.StockCentralAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockCentralRequestDto;
import com.officine.losto.s7.stocks.dto.StockCentralResponseDto;

import java.util.List;

public interface StockCentralService {

	List<StockCentralResponseDto> listAll();

	StockCentralResponseDto getById(long id);

	List<StockCentralResponseDto> listByMagasinCentral(long magasinCentralId);

	List<StockCentralResponseDto> listBySite(long siteId);

	StockCentralResponseDto create(StockCentralRequestDto dto);

	StockCentralResponseDto update(StockCentralRequestDto dto);

	void delete(long id);

	/** Applique un delta sur la quantité disponible et enregistre un {@link com.officine.losto.entity.MouvementStock}. */
	StockCentralResponseDto adjustDisponible(StockCentralAdjustDisponibleDto dto);
}
