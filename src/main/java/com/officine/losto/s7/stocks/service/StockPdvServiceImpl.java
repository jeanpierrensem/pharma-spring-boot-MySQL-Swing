package com.officine.losto.s7.stocks.service;

import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.entity.Batch;
import com.officine.losto.model.BatchRepo;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s7.stocks.dto.StockPdvAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockPdvRequestDto;
import com.officine.losto.s7.stocks.dto.StockPdvResponseDto;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.mapper.StocksMapper;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPdvServiceImpl implements StockPdvService {

	private final StockPdvRepository stockPdvRepository;
	private final MouvementStockRepository mouvementStockRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final ProductRepo productRepo;
	private final BatchRepo batchRepo;
	private final SiteRepository siteRepository;
	private final UserRepo userRepo;
	private final StocksMapper stocksMapper;

	@Override
	@Transactional(readOnly = true)
	public List<StockPdvResponseDto> listAll() {
		return stockPdvRepository.findAll().stream()
				.map(stocksMapper::toStockPdvResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public StockPdvResponseDto getById(long id) {
		StockPdv s = stockPdvRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StockPdv", id));
		return stocksMapper.toStockPdvResponse(s);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockPdvResponseDto> listByPointDeVente(long pointDeVenteId) {
		if (!pointDeVenteRepository.existsById(pointDeVenteId)) {
			throw new ResourceNotFoundException("PointDeVente", pointDeVenteId);
		}
		return stockPdvRepository.findByPointDeVente_Id(pointDeVenteId).stream()
				.map(stocksMapper::toStockPdvResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public StockPdvResponseDto create(StockPdvRequestDto dto) {
		PointDeVente pdv = pointDeVenteRepository.findById(dto.getPointDeVenteId())
				.orElseThrow(() -> new ResourceNotFoundException("PointDeVente", dto.getPointDeVenteId()));
		Product product = productRepo.findById(dto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));
		if (stockPdvRepository.findByPointDeVente_IdAndProduct_Id(pdv.getId(), product.getId()).isPresent()) {
			throw new IllegalArgumentException(
					"Ligne de stock PDV déjà existante pour ce point de vente et ce produit.");
		}
		StockPdv s = StockPdv.builder()
				.pointDeVente(pdv)
				.product(product)
				.qteDisponible(nz(dto.getQteDisponible()))
				.qteReservee(nz(dto.getQteReservee()))
				.qteSeuilAlerte(dto.getQteSeuilAlerte())
				.updatedAt(LocalDateTime.now())
				.build();
		StockPdv saved = stockPdvRepository.save(s);
		int qte = nz(dto.getQteDisponible());
		if (qte > 0) {
			Batch batch = dto.getBatchId() == null ? null : batchRepo.findById(dto.getBatchId()).orElse(null);
			MouvementStock mv = MouvementStock.builder()
					.product(product)
					.batch(batch)
					.typeMouvement(TypeMouvementStock.ENTREE)
					.quantiteAlgebrique(qte)
					.costPrice(dto.getCostPrice())
					.sellPrice(dto.getSellPrice())
					.pointDeVente(pdv)
					.site(pdv.getSite())
					.dateMouvement(LocalDateTime.now())
					.commentaire("Mise en stock PDV")
					.build();
			mouvementStockRepository.save(mv);
		}
		return stocksMapper.toStockPdvResponse(saved);
	}

	@Override
	@Transactional
	public StockPdvResponseDto update(StockPdvRequestDto dto) {
		StockPdv s = stockPdvRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("StockPdv", dto.getId()));
		if (dto.getQteDisponible() != null) {
			s.setQteDisponible(dto.getQteDisponible());
		}
		if (dto.getQteReservee() != null) {
			s.setQteReservee(dto.getQteReservee());
		}
		if (dto.getQteSeuilAlerte() != null) {
			s.setQteSeuilAlerte(dto.getQteSeuilAlerte());
		}
		s.setUpdatedAt(LocalDateTime.now());
		return stocksMapper.toStockPdvResponse(stockPdvRepository.save(s));
	}

	@Override
	@Transactional
	public void delete(long id) {
		StockPdv s = stockPdvRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StockPdv", id));
		stockPdvRepository.delete(s);
	}

	@Override
	@Transactional
	public StockPdvResponseDto adjustDisponible(StockPdvAdjustDisponibleDto dto) {
		if (dto.getDelta() == 0) {
			throw new IllegalArgumentException("Le delta ne peut pas être 0.");
		}
		PointDeVente pdv = pointDeVenteRepository.findById(dto.getPointDeVenteId())
				.orElseThrow(() -> new ResourceNotFoundException("PointDeVente", dto.getPointDeVenteId()));
		Product product = productRepo.findById(dto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));

		StockPdv row = stockPdvRepository
				.findByPointDeVente_IdAndProduct_Id(pdv.getId(), product.getId())
				.orElseGet(() -> StockPdv.builder()
						.pointDeVente(pdv)
						.product(product)
						.qteDisponible(0)
						.qteReservee(0)
						.build());

		int base = row.getQteDisponible() == null ? 0 : row.getQteDisponible();
		int next = base + dto.getDelta();
		if (next < 0) {
			throw new IllegalArgumentException("Stock disponible insuffisant pour cette opération.");
		}
		row.setQteDisponible(next);
		row.setUpdatedAt(LocalDateTime.now());

		if (row.getQteSeuilAlerte() != null && next < row.getQteSeuilAlerte()) {
			log.warn("Stock PDV pdv={} produit={} : disponible {} < seuil alerte {}",
					pdv.getId(), product.getId(), next, row.getQteSeuilAlerte());
		}

		StockPdv saved = stockPdvRepository.save(row);

		Batch batch = dto.getBatchId() == null
				? null
				: batchRepo.findById(dto.getBatchId()).orElse(null);
		MouvementStock mv = MouvementStock.builder()
				.product(product)
				.batch(batch)
				.typeMouvement(dto.getTypeMouvement())
				.quantiteAlgebrique(dto.getDelta())
				.costPrice(dto.getCostPrice())
				.sellPrice(dto.getSellPrice())
				.referenceType(dto.getReferenceType())
				.referenceId(dto.getReferenceId())
				.site(dto.getSiteId() != null ? siteRepository.getReferenceById(dto.getSiteId()) : pdv.getSite())
				.pointDeVente(pdv)
				.appUser(dto.getUserId() != null ? userRepo.getReferenceById(dto.getUserId()) : null)
				.dateMouvement(LocalDateTime.now())
				.commentaire(dto.getCommentaire())
				.build();
		mouvementStockRepository.save(mv);

		return stocksMapper.toStockPdvResponse(saved);
	}

	private static int nz(Integer v) {
		return v == null ? 0 : v;
	}
}
