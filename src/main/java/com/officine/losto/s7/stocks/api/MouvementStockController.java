package com.officine.losto.s7.stocks.api;

import com.officine.losto.s7.stocks.dto.MouvementStockResponseDto;
import com.officine.losto.s7.stocks.service.MouvementStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/mouvements-stock", produces = "application/json")
@RequiredArgsConstructor
public class MouvementStockController {

	private final MouvementStockService mouvementStockService;

	@GetMapping
	public List<MouvementStockResponseDto> list() {
		return mouvementStockService.listAll();
	}

	@GetMapping("/{id}")
	public MouvementStockResponseDto getById(@PathVariable long id) {
		return mouvementStockService.getById(id);
	}

	@GetMapping("/by-product/{productId}")
	public List<MouvementStockResponseDto> listByProduct(@PathVariable long productId) {
		return mouvementStockService.listByProduct(productId);
	}

	@GetMapping("/by-site/{siteId}")
	public List<MouvementStockResponseDto> listBySite(@PathVariable long siteId) {
		return mouvementStockService.listBySite(siteId);
	}

	@GetMapping("/by-point-de-vente/{pointDeVenteId}")
	public List<MouvementStockResponseDto> listByPdv(@PathVariable long pointDeVenteId) {
		return mouvementStockService.listByPointDeVente(pointDeVenteId);
	}
}
