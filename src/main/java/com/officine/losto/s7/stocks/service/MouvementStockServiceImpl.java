package com.officine.losto.s7.stocks.service;

import com.officine.losto.entity.MouvementStock;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s7.stocks.dto.MouvementStockResponseDto;
import com.officine.losto.s7.stocks.mapper.StocksMapper;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MouvementStockServiceImpl implements MouvementStockService {

	private final MouvementStockRepository mouvementStockRepository;
	private final ProductRepo productRepo;
	private final SiteRepository siteRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final StocksMapper stocksMapper;

	@Override
	@Transactional(readOnly = true)
	public List<MouvementStockResponseDto> listAll() {
		return mouvementStockRepository.findAll().stream()
				.map(stocksMapper::toMouvementResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public MouvementStockResponseDto getById(long id) {
		MouvementStock m = mouvementStockRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MouvementStock", id));
		return stocksMapper.toMouvementResponse(m);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MouvementStockResponseDto> listByProduct(long productId) {
		if (!productRepo.existsById(productId)) {
			throw new ResourceNotFoundException("Product", productId);
		}
		return mouvementStockRepository.findByProduct_IdOrderByDateMouvementDesc(productId).stream()
				.map(stocksMapper::toMouvementResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MouvementStockResponseDto> listBySite(long siteId) {
		if (!siteRepository.existsById(siteId)) {
			throw new ResourceNotFoundException("Site", siteId);
		}
		return mouvementStockRepository.findBySite_IdOrderByDateMouvementDesc(siteId).stream()
				.map(stocksMapper::toMouvementResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MouvementStockResponseDto> listByPointDeVente(long pointDeVenteId) {
		if (!pointDeVenteRepository.existsById(pointDeVenteId)) {
			throw new ResourceNotFoundException("PointDeVente", pointDeVenteId);
		}
		return mouvementStockRepository.findByPointDeVente_IdOrderByDateMouvementDesc(pointDeVenteId).stream()
				.map(stocksMapper::toMouvementResponse)
				.collect(Collectors.toList());
	}
}
