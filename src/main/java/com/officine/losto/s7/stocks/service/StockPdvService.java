package com.officine.losto.s7.stocks.service;

import com.officine.losto.s7.stocks.dto.StockPdvAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockPdvRequestDto;
import com.officine.losto.s7.stocks.dto.StockPdvResponseDto;

import java.util.List;

public interface StockPdvService {

	List<StockPdvResponseDto> listAll();

	StockPdvResponseDto getById(long id);

	List<StockPdvResponseDto> listByPointDeVente(long pointDeVenteId);

	StockPdvResponseDto create(StockPdvRequestDto dto);

	StockPdvResponseDto update(StockPdvRequestDto dto);

	void delete(long id);

	StockPdvResponseDto adjustDisponible(StockPdvAdjustDisponibleDto dto);
}
