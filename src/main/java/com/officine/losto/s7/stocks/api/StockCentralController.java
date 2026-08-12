package com.officine.losto.s7.stocks.api;

import com.officine.losto.s7.stocks.dto.StockCentralAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockCentralRequestDto;
import com.officine.losto.s7.stocks.dto.StockCentralResponseDto;
import com.officine.losto.s7.stocks.service.StockCentralService;
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
@RequestMapping(value = "/api/stock-central", produces = "application/json")
@RequiredArgsConstructor
public class StockCentralController {

	private final StockCentralService stockCentralService;

	@GetMapping
	public List<StockCentralResponseDto> list() {
		return stockCentralService.listAll();
	}

	@GetMapping("/{id}")
	public StockCentralResponseDto getById(@PathVariable long id) {
		return stockCentralService.getById(id);
	}

	@GetMapping("/by-magasin/{magasinCentralId}")
	public List<StockCentralResponseDto> listByMagasin(@PathVariable long magasinCentralId) {
		return stockCentralService.listByMagasinCentral(magasinCentralId);
	}

	@GetMapping("/by-site/{siteId}")
	public List<StockCentralResponseDto> listBySite(@PathVariable long siteId) {
		return stockCentralService.listBySite(siteId);
	}

	@PostMapping
	public StockCentralResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class }) @RequestBody StockCentralRequestDto dto) {
		return stockCentralService.create(dto);
	}

	@PutMapping
	public StockCentralResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class }) @RequestBody StockCentralRequestDto dto) {
		return stockCentralService.update(dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		stockCentralService.delete(id);
	}

	@PostMapping("/adjust-disponible")
	public StockCentralResponseDto adjustDisponible(@Validated @RequestBody StockCentralAdjustDisponibleDto dto) {
		return stockCentralService.adjustDisponible(dto);
	}
}
