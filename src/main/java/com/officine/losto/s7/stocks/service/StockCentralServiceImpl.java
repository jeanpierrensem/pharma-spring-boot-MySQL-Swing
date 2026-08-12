package com.officine.losto.s7.stocks.service;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.entity.Batch;
import com.officine.losto.model.BatchRepo;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s7.stocks.dto.StockCentralAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockCentralRequestDto;
import com.officine.losto.s7.stocks.dto.StockCentralResponseDto;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.mapper.StocksMapper;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
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
public class StockCentralServiceImpl implements StockCentralService {

	private final StockCentralRepository stockCentralRepository;
	private final MouvementStockRepository mouvementStockRepository;
	private final MagasinCentralRepository magasinCentralRepository;
	private final ProductRepo productRepo;
	private final BatchRepo batchRepo;
	private final SiteRepository siteRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final UserRepo userRepo;
	private final StocksMapper stocksMapper;

	@Override
	@Transactional(readOnly = true)
	public List<StockCentralResponseDto> listAll() {
		return stockCentralRepository.findAll().stream()
				.map(stocksMapper::toStockCentralResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public StockCentralResponseDto getById(long id) {
		StockCentral s = stockCentralRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StockCentral", id));
		return stocksMapper.toStockCentralResponse(s);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockCentralResponseDto> listByMagasinCentral(long magasinCentralId) {
		if (!magasinCentralRepository.existsById(magasinCentralId)) {
			throw new ResourceNotFoundException("MagasinCentral", magasinCentralId);
		}
		return stockCentralRepository.findByMagasinCentral_Id(magasinCentralId).stream()
				.map(stocksMapper::toStockCentralResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockCentralResponseDto> listBySite(long siteId) {
		if (!siteRepository.existsById(siteId)) {
			throw new ResourceNotFoundException("Site", siteId);
		}
		return stockCentralRepository.findBySite_Id(siteId).stream()
				.map(stocksMapper::toStockCentralResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public StockCentralResponseDto create(StockCentralRequestDto dto) {
		MagasinCentral magasin = magasinCentralRepository.findById(dto.getMagasinCentralId())
				.orElseThrow(() -> new ResourceNotFoundException("MagasinCentral", dto.getMagasinCentralId()));
		Product product = productRepo.findById(dto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));
		if (dto.getBatchId() == null) {
			throw new IllegalArgumentException("Le lot est obligatoire pour une ligne de stock central.");
		}
		Batch batch = batchRepo.findById(dto.getBatchId())
				.orElseThrow(() -> new ResourceNotFoundException("Batch", dto.getBatchId()));
		if (stockCentralRepository.existsByMagasinCentral_IdAndProduct_IdAndBatch_Id(
				magasin.getId(), product.getId(), batch.getId())) {
			throw new IllegalArgumentException(
					"Ligne de stock central déjà existante pour ce magasin, ce produit et ce lot.");
		}
		StockCentral s = StockCentral.builder()
				.site(magasin.getSite())
				.magasinCentral(magasin)
				.product(product)
				.batch(batch)
				.qteDisponible(nz(dto.getQteDisponible()))
				.qteReservee(nz(dto.getQteReservee()))
				.qteSeuilAlerte(dto.getQteSeuilAlerte())
				.updatedAt(LocalDateTime.now())
				.build();
		StockCentral saved = stockCentralRepository.save(s);
		int qte = nz(dto.getQteDisponible());
		if (qte > 0) {
			recordInitialMovement(magasin, product, batch, qte, dto.getCostPrice(), dto.getSellPrice(),
					magasin.getSite() != null ? magasin.getSite().getId() : null);
		}
		return stocksMapper.toStockCentralResponse(saved);
	}

	private void recordInitialMovement(
			MagasinCentral magasin,
			Product product,
			Batch batch,
			int quantite,
			java.math.BigDecimal costPrice,
			java.math.BigDecimal sellPrice,
			Long siteId) {
		MouvementStock mv = MouvementStock.builder()
				.product(product)
				.batch(batch)
				.typeMouvement(TypeMouvementStock.ENTREE)
				.quantiteAlgebrique(quantite)
				.costPrice(costPrice)
				.sellPrice(sellPrice)
				.site(siteId != null ? siteRepository.getReferenceById(siteId) : magasin.getSite())
				.dateMouvement(LocalDateTime.now())
				.commentaire("Mise en stock central")
				.build();
		mouvementStockRepository.save(mv);
	}

	@Override
	@Transactional
	public StockCentralResponseDto update(StockCentralRequestDto dto) {
		StockCentral s = stockCentralRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("StockCentral", dto.getId()));
		if (dto.getQteDisponible() != null) {
			s.setQteDisponible(dto.getQteDisponible());
		}
		if (dto.getQteReservee() != null) {
			s.setQteReservee(dto.getQteReservee());
		}
		if (dto.getQteSeuilAlerte() != null) {
			s.setQteSeuilAlerte(dto.getQteSeuilAlerte());
		}
		if (dto.getCostPrice() != null) {
			s.setCostPrice(dto.getCostPrice());
		}
		if (dto.getSellPrice() != null) {
			s.setSellPrice(dto.getSellPrice());
		}
		s.setUpdatedAt(LocalDateTime.now());
		return stocksMapper.toStockCentralResponse(stockCentralRepository.save(s));
	}

	@Override
	@Transactional
	public void delete(long id) {
		StockCentral s = stockCentralRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StockCentral", id));
		stockCentralRepository.delete(s);
	}

	@Override
	@Transactional
	public StockCentralResponseDto adjustDisponible(StockCentralAdjustDisponibleDto dto) {
		if (dto.getDelta() == 0) {
			throw new IllegalArgumentException("Le delta ne peut pas être 0.");
		}
		if (dto.getBatchId() == null) {
			throw new IllegalArgumentException("Le lot est obligatoire pour ajuster le stock central.");
		}
		MagasinCentral magasin = magasinCentralRepository.findById(dto.getMagasinCentralId())
				.orElseThrow(() -> new ResourceNotFoundException("MagasinCentral", dto.getMagasinCentralId()));
		Product product = productRepo.findById(dto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));
		Batch batch = batchRepo.findById(dto.getBatchId())
				.orElseThrow(() -> new ResourceNotFoundException("Batch", dto.getBatchId()));

		StockCentral row = stockCentralRepository
				.findByMagasinCentral_IdAndProduct_IdAndBatch_Id(magasin.getId(), product.getId(), batch.getId())
				.orElseGet(() -> StockCentral.builder()
						.magasinCentral(magasin)
						.product(product)
						.batch(batch)
						.site(magasin.getSite())
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
		if (dto.getCostPrice() != null) {
			row.setCostPrice(dto.getCostPrice());
		}
		if (dto.getSellPrice() != null) {
			row.setSellPrice(dto.getSellPrice());
		}
		if (row.getSite() == null) {
			row.setSite(magasin.getSite());
		}

		if (row.getQteSeuilAlerte() != null && next < row.getQteSeuilAlerte()) {
			log.warn("Stock central magasin={} produit={} : disponible {} < seuil alerte {}",
					magasin.getId(), product.getId(), next, row.getQteSeuilAlerte());
		}

		StockCentral saved = stockCentralRepository.save(row);

		MouvementStock mv = MouvementStock.builder()
				.product(product)
				.batch(batch)
				.typeMouvement(dto.getTypeMouvement())
				.quantiteAlgebrique(dto.getDelta())
				.costPrice(dto.getCostPrice())
				.sellPrice(dto.getSellPrice())
				.referenceType(dto.getReferenceType())
				.referenceId(dto.getReferenceId())
				.site(dto.getSiteId() != null ? siteRepository.getReferenceById(dto.getSiteId()) : magasin.getSite())
				.pointDeVente(dto.getPointDeVenteId() != null
						? pointDeVenteRepository.getReferenceById(dto.getPointDeVenteId())
						: null)
				.appUser(dto.getUserId() != null ? userRepo.getReferenceById(dto.getUserId()) : null)
				.dateMouvement(LocalDateTime.now())
				.commentaire(dto.getCommentaire())
				.build();
		mouvementStockRepository.save(mv);

		return stocksMapper.toStockCentralResponse(saved);
	}

	private static int nz(Integer v) {
		return v == null ? 0 : v;
	}
}
