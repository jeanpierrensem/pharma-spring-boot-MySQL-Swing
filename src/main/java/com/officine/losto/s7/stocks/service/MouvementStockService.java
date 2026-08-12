package com.officine.losto.s7.stocks.service;

import com.officine.losto.s7.stocks.dto.MouvementStockResponseDto;

import java.util.List;

public interface MouvementStockService {

	List<MouvementStockResponseDto> listAll();

	MouvementStockResponseDto getById(long id);

	List<MouvementStockResponseDto> listByProduct(long productId);

	List<MouvementStockResponseDto> listBySite(long siteId);

	List<MouvementStockResponseDto> listByPointDeVente(long pointDeVenteId);
}
