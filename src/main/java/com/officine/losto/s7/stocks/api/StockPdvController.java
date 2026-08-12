package com.officine.losto.s7.stocks.api;

import com.officine.losto.s7.stocks.dto.StockPdvAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockPdvRequestDto;
import com.officine.losto.s7.stocks.dto.StockPdvResponseDto;
import com.officine.losto.s7.stocks.service.StockPdvService;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/stock-pdv", produces = "application/json")
@RequiredArgsConstructor
public class StockPdvController {

	private final StockPdvService stockPdvService;

	@GetMapping
	public List<StockPdvResponseDto> list() {
		return stockPdvService.listAll();
	}

	@GetMapping("/{id}")
	public StockPdvResponseDto getById(@PathVariable long id) {
		return stockPdvService.getById(id);
	}

	@GetMapping("/by-point-de-vente/{pointDeVenteId}")
	public List<StockPdvResponseDto> listByPdv(@PathVariable long pointDeVenteId) {
		return stockPdvService.listByPointDeVente(pointDeVenteId);
	}

	@PostMapping
	public StockPdvResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class }) @RequestBody StockPdvRequestDto dto) {
		return stockPdvService.create(dto);
	}

	@PutMapping
	public StockPdvResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class }) @RequestBody StockPdvRequestDto dto) {
		return stockPdvService.update(dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		stockPdvService.delete(id);
	}

	@PostMapping("/adjust-disponible")
	public StockPdvResponseDto adjustDisponible(@Validated @RequestBody StockPdvAdjustDisponibleDto dto) {
		return stockPdvService.adjustDisponible(dto);
	}
}
