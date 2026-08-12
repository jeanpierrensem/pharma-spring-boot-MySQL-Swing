package com.officine.losto.config;

import com.officine.losto.entity.Batch;
import com.officine.losto.entity.AppUser;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Site;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.entity.Threshold;
import com.officine.losto.model.BatchRepo;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.ThresholdRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s7.stocks.domain.ReferenceStockType;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;

import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Données de démonstration pour {@code STOCK_CENTRAL}, {@code STOCK_PDV}, {@code MOUVEMENT_STOCK}
 * (profil {@code dev} uniquement). Idempotent : saute si les lignes de stock central attendues existent déjà.
 */
@Configuration
@Profile("dev")
public class DemoStockSeedConfig {

	/** Seuil quantité « stock bas » — associé à chaque produit et copié dans {@code qteSeuilAlerte}. */
	public static final String THR_STOCK_BAS_CODE = "THR-STOCK-BAS";

	private static final String SEED_SITE_CODE = "SEED-STOCK-SITE";
	private static final String SEED_MC_CODE = "SEED-STOCK-MC";
	private static final String SEED_PDV1_CODE = "SEED-STOCK-PDV1";
	private static final String SEED_PDV2_CODE = "SEED-STOCK-PDV2";

	@Bean
	@Order(20)
	CommandLineRunner seedDemoStock(
			@Value("${officine.seed.demo-stock:true}") boolean enabled,
			SiteRepository siteRepository,
			MagasinCentralRepository magasinCentralRepository,
			PointDeVenteRepository pointDeVenteRepository,
			ProductRepo productRepo,
			BatchRepo batchRepo,
			StockCentralRepository stockCentralRepository,
			StockPdvRepository stockPdvRepository,
			MouvementStockRepository mouvementStockRepository,
			UserRepo userRepo) {

		return args -> {
			if (!enabled) {
				return;
			}
			List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			if (products.size() < 2) {
				return;
			}
			Product p1 = products.get(0);
			Product p2 = products.get(1);

			MagasinContext ctx = resolveMagasinCentral(siteRepository, magasinCentralRepository);
			MagasinCentral mc = ctx.mc();
			Long siteId = magasinCentralRepository.findById(mc.getId()).orElseThrow().getSite().getId();
			Site site = siteRepository.findById(siteId).orElseThrow();

			if (ctx.freshOrg()) {
				ensurePointDeVente(pointDeVenteRepository, site, SEED_PDV1_CODE, "Caisse démo stock 1");
				ensurePointDeVente(pointDeVenteRepository, site, SEED_PDV2_CODE, "Caisse démo stock 2");
			}
			List<PointDeVente> pdvs = pointDeVenteRepository.findBySite_Id(site.getId());
			if (pdvs.size() < 2) {
				return;
			}
			PointDeVente pdv1 = pdvs.get(0);
			PointDeVente pdv2 = pdvs.get(1);

			if (!stockCentralRepository.findByMagasinCentral_Id(mc.getId()).isEmpty()) {
				return;
			}

			LocalDateTime now = LocalDateTime.now();
			List<Batch> batches = batchRepo.findAll();
			if (batches.isEmpty()) {
				return;
			}
			Batch lotA = batches.get(0);
			Batch lotB = batches.size() > 1 ? batches.get(1) : lotA;
			ThreadLocalRandom rnd = ThreadLocalRandom.current();
			BigDecimal p1CostA = randomCost(rnd);
			BigDecimal p1SellA = randomSell(rnd, p1CostA);
			BigDecimal p1CostB = randomCost(rnd);
			BigDecimal p1SellB = randomSell(rnd, p1CostB);
			BigDecimal p2Cost = randomCost(rnd);
			BigDecimal p2Sell = randomSell(rnd, p2Cost);

			List<StockCentral> centralRows = new ArrayList<>();
			centralRows.add(StockCentral.builder()
					.site(site)
					.magasinCentral(mc)
					.product(p1)
					.batch(lotA)
					.qteDisponible(120)
					.costPrice(p1CostA)
					.sellPrice(p1SellA)
					.qteReservee(5)
					.qteSeuilAlerte(25)
					.updatedAt(now)
					.build());
			centralRows.add(StockCentral.builder()
					.site(site)
					.magasinCentral(mc)
					.product(p1)
					.batch(lotB)
					.qteDisponible(35)
					.costPrice(p1CostB)
					.sellPrice(p1SellB)
					.qteReservee(0)
					.qteSeuilAlerte(25)
					.updatedAt(now)
					.build());
			centralRows.add(StockCentral.builder()
					.site(site)
					.magasinCentral(mc)
					.product(p2)
					.batch(lotA)
					.qteDisponible(8)
					.costPrice(p2Cost)
					.sellPrice(p2Sell)
					.qteReservee(2)
					.qteSeuilAlerte(15)
					.updatedAt(now)
					.build());
			if (products.size() > 2) {
				Product p3 = products.get(2);
				BigDecimal p3CostA = randomCost(rnd);
				BigDecimal p3SellA = randomSell(rnd, p3CostA);
				BigDecimal p3CostB = randomCost(rnd);
				BigDecimal p3SellB = randomSell(rnd, p3CostB);
				centralRows.add(StockCentral.builder()
						.site(site)
						.magasinCentral(mc)
						.product(p3)
						.batch(lotA)
						.qteDisponible(45)
						.costPrice(p3CostA)
						.sellPrice(p3SellA)
						.qteReservee(0)
						.qteSeuilAlerte(10)
						.updatedAt(now)
						.build());
				centralRows.add(StockCentral.builder()
						.site(site)
						.magasinCentral(mc)
						.product(p3)
						.batch(lotB)
						.qteDisponible(18)
						.costPrice(p3CostB)
						.sellPrice(p3SellB)
						.qteReservee(0)
						.qteSeuilAlerte(10)
						.updatedAt(now)
						.build());
			}
			stockCentralRepository.saveAll(centralRows);

			List<StockPdv> pdvRows = new ArrayList<>();
			pdvRows.add(StockPdv.builder()
					.pointDeVente(pdv1)
					.product(p1)
					.qteDisponible(24)
					.qteReservee(4)
					.qteSeuilAlerte(8)
					.updatedAt(now)
					.build());
			pdvRows.add(StockPdv.builder()
					.pointDeVente(pdv1)
					.product(p2)
					.qteDisponible(6)
					.qteReservee(0)
					.qteSeuilAlerte(12)
					.updatedAt(now)
					.build());
			pdvRows.add(StockPdv.builder()
					.pointDeVente(pdv2)
					.product(p1)
					.qteDisponible(15)
					.qteReservee(0)
					.qteSeuilAlerte(5)
					.updatedAt(now)
					.build());
			stockPdvRepository.saveAll(pdvRows);

			AppUser actor = userRepo.findAll().stream().findFirst().orElse(null);

			List<MouvementStock> mvts = new ArrayList<>();
			mvts.add(MouvementStock.builder()
					.product(p1)
					.typeMouvement(TypeMouvementStock.ENTREE)
					.quantiteAlgebrique(50)
					.referenceType(ReferenceStockType.BON_INTERNE)
					.site(site)
					.pointDeVente(pdv1)
					.appUser(actor)
					.dateMouvement(now.minusDays(2))
					.commentaire("Seed dev — entrée magasin")
					.build());
			mvts.add(MouvementStock.builder()
					.product(p1)
					.typeMouvement(TypeMouvementStock.SORTIE)
					.quantiteAlgebrique(-12)
					.referenceType(ReferenceStockType.VENTE)
					.site(site)
					.pointDeVente(pdv1)
					.appUser(actor)
					.dateMouvement(now.minusDays(1))
					.commentaire("Seed dev — sortie caisse")
					.build());
			mvts.add(MouvementStock.builder()
					.product(p2)
					.typeMouvement(TypeMouvementStock.AJUSTEMENT_INVENTAIRE)
					.quantiteAlgebrique(-3)
					.referenceType(ReferenceStockType.INVENTAIRE)
					.site(site)
					.appUser(actor)
					.dateMouvement(now.minusHours(5))
					.commentaire("Seed dev — ajustement inventaire")
					.build());
			mouvementStockRepository.saveAll(mvts);
		};
	}

	/**
	 * Alimente le magasin central : jusqu'à 2 lignes {@link StockCentral} par produit (lots distincts),
	 * quantités aléatoires, réserve 0, seuil = {@value #THR_STOCK_BAS_CODE}. Recrée le stock central du
	 * magasin si {@code officine.seed.central-stock-reset=true} (défaut).
	 */
	@Bean
	@Order(21)
	CommandLineRunner seedAllProductsCentralStock(
			@Value("${officine.seed.central-stock-all-products:true}") boolean enabled,
			@Value("${officine.seed.central-stock-reset:true}") boolean resetExisting,
			@Value("${officine.seed.central-stock-qty-min:1}") int qtyMin,
			@Value("${officine.seed.central-stock-qty-max:500}") int qtyMax,
			SiteRepository siteRepository,
			MagasinCentralRepository magasinCentralRepository,
			ProductRepo productRepo,
			BatchRepo batchRepo,
			ThresholdRepo thresholdRepo,
			StockCentralRepository stockCentralRepository,
			TransactionTemplate transactionTemplate) {

		return args -> transactionTemplate.executeWithoutResult(status -> {
			if (!enabled) {
				return;
			}
			Threshold stockBas = thresholdRepo.findByCode(THR_STOCK_BAS_CODE);
			if (stockBas == null) {
				return;
			}
			List<Batch> batches = batchRepo.findAll();
			if (batches.isEmpty()) {
				return;
			}
			int seuilLevel = stockBas.getLevel() != null ? stockBas.getLevel() : 0;
			int min = Math.min(qtyMin, qtyMax);
			int max = Math.max(qtyMin, qtyMax);
			if (max < 1) {
				max = 1;
			}
			if (min < 1) {
				min = 1;
			}

			MagasinContext ctx = resolveMagasinCentral(siteRepository, magasinCentralRepository);
			MagasinCentral mc = ctx.mc();
			Site site = magasinCentralRepository.findById(mc.getId()).orElseThrow().getSite();
			Long mcId = mc.getId();

			List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			if (products.isEmpty()) {
				return;
			}

			if (resetExisting) {
				stockCentralRepository.deleteByMagasinCentral_Id(mcId);
			}

			ThreadLocalRandom rnd = ThreadLocalRandom.current();
			LocalDateTime now = LocalDateTime.now();
			List<StockCentral> newRows = new ArrayList<>();
			List<Product> productsToUpdateThreshold = new ArrayList<>();

			for (int i = 0; i < products.size(); i++) {
				Product product = products.get(i);
				boolean hasStockBas = product.getThresholds() != null
						&& product.getThresholds().stream()
								.anyMatch(t -> THR_STOCK_BAS_CODE.equals(t.getCode()));
				if (!hasStockBas) {
					if (product.getThresholds() == null) {
						product.setThresholds(new HashSet<>());
					}
					product.getThresholds().add(stockBas);
					productsToUpdateThreshold.add(product);
				}
				Batch lot1 = batches.get(i % batches.size());
				Batch lot2 = batches.get((i + 1) % batches.size());
				BigDecimal lot1Cost = randomCost(rnd);
				BigDecimal lot1Sell = randomSell(rnd, lot1Cost);
				addCentralRowIfAbsent(
						newRows, mcId, mc, site, product, lot1, rnd.nextInt(min, max + 1), lot1Cost, lot1Sell,
						seuilLevel, now, stockCentralRepository, resetExisting);
				if (!lot2.getId().equals(lot1.getId())) {
					BigDecimal lot2Cost = randomCost(rnd);
					BigDecimal lot2Sell = randomSell(rnd, lot2Cost);
					addCentralRowIfAbsent(
							newRows,
							mcId,
							mc,
							site,
							product,
							lot2,
							rnd.nextInt(min, max + 1),
							lot2Cost,
							lot2Sell,
							seuilLevel,
							now,
							stockCentralRepository,
							resetExisting);
				}
			}

			if (!productsToUpdateThreshold.isEmpty()) {
				productRepo.saveAll(productsToUpdateThreshold);
			}
			if (!newRows.isEmpty()) {
				stockCentralRepository.saveAll(newRows);
			}
		});
	}

	private static void addCentralRowIfAbsent(
			List<StockCentral> newRows,
			Long mcId,
			MagasinCentral mc,
			Site site,
			Product product,
			Batch batch,
			int qty,
			BigDecimal costPrice,
			BigDecimal sellPrice,
			int seuilLevel,
			LocalDateTime now,
			StockCentralRepository stockCentralRepository,
			boolean resetExisting) {
		if (!resetExisting
				&& stockCentralRepository.existsByMagasinCentral_IdAndProduct_IdAndBatch_Id(
						mcId, product.getId(), batch.getId())) {
			return;
		}
		newRows.add(
				StockCentral.builder()
						.site(site)
						.magasinCentral(mc)
						.product(product)
						.batch(batch)
						.qteDisponible(qty)
						.costPrice(costPrice)
						.sellPrice(sellPrice)
						.qteReservee(0)
						.qteSeuilAlerte(seuilLevel)
						.updatedAt(now)
						.build());
	}

	private static BigDecimal randomCost(ThreadLocalRandom rnd) {
		return BigDecimal.valueOf(rnd.nextDouble(0.5, 28.0)).setScale(2, RoundingMode.HALF_UP);
	}

	private static BigDecimal randomSell(ThreadLocalRandom rnd, BigDecimal cost) {
		return cost.multiply(BigDecimal.valueOf(1.12 + rnd.nextDouble(0.08, 0.55)))
				.setScale(2, RoundingMode.HALF_UP);
	}

	private record MagasinContext(MagasinCentral mc, boolean freshOrg) {
	}

	private static MagasinContext resolveMagasinCentral(
			SiteRepository siteRepository,
			MagasinCentralRepository magasinCentralRepository) {

		List<MagasinCentral> existing = magasinCentralRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		if (!existing.isEmpty()) {
			MagasinCentral mc = magasinCentralRepository.findById(existing.get(0).getId()).orElseThrow();
			return new MagasinContext(mc, false);
		}

		Site site = siteRepository.findByCode(SEED_SITE_CODE).orElseGet(() -> siteRepository.save(
				Site.builder()
						.code(SEED_SITE_CODE)
						.libelle("Site de démonstration — stock")
						.actif(true)
						.build()));

		MagasinCentral mc = magasinCentralRepository.findBySite_Id(site.getId()).orElseGet(() -> magasinCentralRepository.save(
				MagasinCentral.builder()
						.site(site)
						.code(SEED_MC_CODE)
						.libelle("Magasin central démo stock")
						.build()));
		return new MagasinContext(mc, true);
	}

	private static void ensurePointDeVente(
			PointDeVenteRepository pointDeVenteRepository,
			Site site,
			String code,
			String libelle) {

		if (!pointDeVenteRepository.existsByCode(code)) {
			pointDeVenteRepository.save(PointDeVente.builder()
					.site(site)
					.code(code)
					.libelle(libelle)
					.actif(true)
					.build());
		}
	}
}
