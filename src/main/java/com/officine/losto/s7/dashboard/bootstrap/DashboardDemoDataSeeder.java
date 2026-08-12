package com.officine.losto.s7.dashboard.bootstrap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.officine.losto.config.DevSeedProductPins;
import com.officine.losto.entity.AppUser;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.LigneBonCommandeInterne;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Sell;
import com.officine.losto.entity.SellDetails;
import com.officine.losto.entity.Site;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.SellRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s5.reappro.repository.BonCommandeInterneRepository;
import com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl;
import com.officine.losto.service.SellService;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.util.LatestProductMovementSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Jeu de démo pour le tableau de bord : ventes et bons internes datés relativement à
 * {@link LocalDate#now()} afin d’alimenter les KPI sur les 30 derniers jours.
 * <p>
 * S’active lorsque le marqueur {@value #MARKER_SELL} est absent (profil {@code dev}).
 * Fonctionne avec n’importe quel site ayant magasin central + PDV (ex. {@code SEED-STOCK-SITE}).
 */
@Component
@Profile("dev")
@Order(315)
@RequiredArgsConstructor
@Slf4j
public class DashboardDemoDataSeeder implements ApplicationRunner {

	static final String MARKER_SELL = "VTE-DASH-001";
	private static final BigDecimal DEMO_COST_RATIO = new BigDecimal("0.65");
	private static final String BON_ENVOYE = "BINT-DASH-001";
	private static final String BON_PARTIEL = "BINT-DASH-002";
	private static final String BON_TRAITE = "BINT-DASH-003";

	private final SellRepo sellRepo;
	private final BonCommandeInterneRepository bonCommandeInterneRepository;
	private final SiteRepository siteRepository;
	private final MagasinCentralRepository magasinCentralRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final UserRepo userRepo;
	private final ProductRepo productRepo;
	private final SellService sellService;
	private final BonCommandeInterneServiceImpl bonCommandeInterneService;
	private final MouvementStockRepository mouvementStockRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (sellRepo.existsByNumber(MARKER_SELL)) {
			recalibrateDemoSellCosts();
			log.debug("Dashboard : jeu démo déjà présent ({}) — coûts recalibrés si besoin.", MARKER_SELL);
			return;
		}
		AppUser vendeur = userRepo.findByLogin("pharma");
		if (vendeur == null) {
			vendeur = userRepo.findAll().stream().findFirst().orElse(null);
		}
		if (vendeur == null) {
			log.warn("Dashboard : aucun utilisateur pour le seed — ignoré.");
			return;
		}
		DevSeedProductPins.Trio products = DevSeedProductPins.resolveDemoProducts(productRepo);
		if (products == null) {
			List<Product> all = productRepo.findAll();
			if (all.size() < 2) {
				log.warn("Dashboard : produits insuffisants — ignoré.");
				return;
			}
			products = new DevSeedProductPins.Trio(all.get(0), all.get(1), all.size() > 2 ? all.get(2) : all.get(1));
		}

		for (Site site : siteRepository.findAll()) {
			Optional<MagasinCentral> mcOpt = magasinCentralRepository.findBySite_Id(site.getId());
			List<PointDeVente> pdvs = pointDeVenteRepository.findBySite_Id(site.getId());
			if (mcOpt.isEmpty() || pdvs.isEmpty()) {
				continue;
			}
			seedSales(site, pdvs, vendeur, products);
			seedBons(site, pdvs.getFirst(), mcOpt.get(), vendeur, products);
			log.info(
					"Dashboard : ventes et bons de démo insérés pour le site {} ({} PDV).",
					site.getCode(),
					pdvs.size());
			return;
		}
		log.warn("Dashboard : aucun site avec magasin central et PDV — seed ignoré.");
	}

	private void seedSales(
			Site site, List<PointDeVente> pdvs, AppUser vendeur, DevSeedProductPins.Trio products) {
		LocalDate today = LocalDate.now();
		PointDeVente pdv1 = pdvs.getFirst();
		PointDeVente pdv2 = pdvs.size() > 1 ? pdvs.get(1) : pdv1;

		saveSell("VTE-DASH-001", today.minusDays(1), site, pdv1, vendeur, bd("45.60"),
				line(products.first(), 3, 0, bd("7.80")));
		saveSell("VTE-DASH-002", today.minusDays(2), site, pdv1, vendeur, bd("15.00"),
				line(products.second(), 2, 0, bd("7.50")));
		saveSell("VTE-DASH-003", today.minusDays(4), site, pdv2, vendeur, bd("36.00"),
				line(products.third(), 3, 0, bd("12.00")));
		saveSell("VTE-DASH-004", today.minusDays(8), site, pdv1, vendeur, bd("62.40"),
				line(products.first(), 4, 5, bd("7.80")), line(products.second(), 2, 0, bd("7.50")));
		saveSell("VTE-DASH-005", today.minusDays(12), site, pdv2, vendeur, bd("52.50"),
				line(products.second(), 7, 0, bd("7.50")));
		saveSell("VTE-DASH-006", today.minusDays(18), site, pdv1, vendeur, bd("96.00"),
				line(products.third(), 8, 0, bd("12.00")));
		saveSell("VTE-DASH-007", today.minusDays(25), site, pdv2, vendeur, bd("23.40"),
				line(products.first(), 2, 0, bd("7.80")), line(products.third(), 1, 0, bd("11.40")));
	}

	private void seedBons(
			Site site,
			PointDeVente pdv,
			MagasinCentral mc,
			AppUser user,
			DevSeedProductPins.Trio products) {
		LocalDate today = LocalDate.now();
		if (!bonCommandeInterneRepository.existsByNumber(BON_ENVOYE)) {
			createBon(BON_ENVOYE, today.minusDays(5), site, pdv, mc, user, products, StatutBonCommandeInterne.ENVOYE);
		}
		if (!bonCommandeInterneRepository.existsByNumber(BON_PARTIEL)) {
			createBon(BON_PARTIEL, today.minusDays(3), site, pdv, mc, user, products, StatutBonCommandeInterne.PARTIEL);
		}
		if (!bonCommandeInterneRepository.existsByNumber(BON_TRAITE)) {
			createBon(BON_TRAITE, today.minusDays(2), site, pdv, mc, user, products, StatutBonCommandeInterne.TRAITE);
		}
	}

	private void createBon(
			String number,
			LocalDate orderDate,
			Site site,
			PointDeVente pdv,
			MagasinCentral mc,
			AppUser user,
			DevSeedProductPins.Trio products,
			StatutBonCommandeInterne target) {
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.number(number)
				.orderDate(orderDate)
				.commentaire("Bon démo tableau de bord")
				.site(site)
				.pointDeVente(pdv)
				.user(user)
				.magasinCentral(mc)
				.lignes(new ArrayList<>())
				.build();
		bon.getLignes()
				.add(LigneBonCommandeInterne.builder()
						.bon(bon)
						.product(products.first())
						.quantity(20)
						.unitPrice(new BigDecimal("2.15"))
						.build());
		bonCommandeInterneService.save(bon);
		if (target != StatutBonCommandeInterne.BROUILLON) {
			bon.setStatut(StatutBonCommandeInterne.ENVOYE);
			bon = bonCommandeInterneService.save(bon, StatutBonCommandeInterne.BROUILLON);
		}
		if (target == StatutBonCommandeInterne.PARTIEL || target == StatutBonCommandeInterne.TRAITE) {
			bon.setStatut(target);
			bonCommandeInterneRepository.save(bon);
		}
	}

	private void saveSell(
			String number,
			LocalDate date,
			Site site,
			PointDeVente pdv,
			AppUser vendeur,
			BigDecimal total,
			SellDetails... lines) {
		Sell sell = Sell.builder()
				.number(number)
				.dateVente(date)
				.seller("Dashboard démo")
				.client("Client comptoir")
				.sellType("COMPTANT")
				.paymentMode("Espèces")
				.paymentStatus("PAYE")
				.totalPrice(total)
				.amountReceived(total)
				.changeGiven(BigDecimal.ZERO)
				.remark("Données démo tableau de bord")
				.site(site)
				.pointDeVente(pdv)
				.effectueePar(vendeur)
				.lignes(new ArrayList<>())
				.build();
		for (SellDetails line : lines) {
			line.setSell(sell);
			sell.getLignes().add(line);
		}
		sellService.save(sell);
	}

	private void recalibrateDemoSellCosts() {
		int fixed = 0;
		for (Sell sell : sellRepo.findAll()) {
			if (sell.getNumber() == null || !sell.getNumber().startsWith("VTE-DASH-")) {
				continue;
			}
			boolean dirty = false;
			for (SellDetails ligne : sell.getLignes()) {
				BigDecimal unitPrice = ligne.getPrice();
				if (unitPrice == null) {
					continue;
				}
				BigDecimal resolved = resolveUnitCostAtSale(unitPrice, ligne.getUnitCostAtSale());
				if (ligne.getUnitCostAtSale() == null || ligne.getUnitCostAtSale().compareTo(resolved) != 0) {
					ligne.setUnitCostAtSale(resolved);
					dirty = true;
					fixed++;
				}
			}
			if (dirty) {
				sellService.save(sell);
			}
		}
		if (fixed > 0) {
			log.info("Dashboard : coûts unitaires démo recalibrés sur {} ligne(s).", fixed);
		}
	}

	private SellDetails line(Product product, int qty, int discountPct, BigDecimal unitPrice) {
		com.officine.losto.entity.Batch batch = null;
		BigDecimal movementCost = null;
		var latest = LatestProductMovementSupport.latest(mouvementStockRepository, product);
		if (latest.isPresent()) {
			var mv = latest.get();
			batch = mv.getBatch();
			movementCost = mv.getCostPrice();
		}
		return SellDetails.builder()
				.product(product)
				.quantity(qty)
				.discount(discountPct)
				.price(unitPrice)
				.batch(batch)
				.unitCostAtSale(resolveUnitCostAtSale(unitPrice, movementCost))
				.build();
	}

	/**
	 * Prix d'achat cohérent pour la démo : si le coût stock (CSV) dépasse le prix de vente
	 * de la ligne, on retient ~65 % du prix de vente (marge démo réaliste).
	 */
	private static BigDecimal resolveUnitCostAtSale(BigDecimal unitPrice, BigDecimal movementCost) {
		BigDecimal fallback = unitPrice.multiply(DEMO_COST_RATIO).setScale(2, RoundingMode.HALF_UP);
		if (movementCost == null || movementCost.compareTo(BigDecimal.ZERO) <= 0) {
			return fallback;
		}
		if (movementCost.compareTo(unitPrice) >= 0) {
			return fallback;
		}
		return movementCost.setScale(2, RoundingMode.HALF_UP);
	}

	private static BigDecimal bd(String s) {
		return new BigDecimal(s);
	}
}
