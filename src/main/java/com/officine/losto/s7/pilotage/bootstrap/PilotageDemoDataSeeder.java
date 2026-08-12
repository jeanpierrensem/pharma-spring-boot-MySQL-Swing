package com.officine.losto.s7.pilotage.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.officine.losto.config.DevSeedProductPins;
import com.officine.losto.entity.AppUser;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Sell;
import com.officine.losto.entity.SellDetails;
import com.officine.losto.entity.Site;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.SellRepo;
import com.officine.losto.model.UserRepo;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.util.LatestProductMovementSupport;
import com.officine.losto.service.SellService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Ventes de démo pour le pilotage (frontend JavaFX / API s7). Jeu étendu : tous les mois de 2026,
 * plusieurs PDV (Paris caisse, Paris ortho si présent, Lyon caisse), années 2025–2026 pour les
 * classements temporels. Idempotence : présence du ticket {@value #FIRST_TICKET}.
 */
@Component
@Profile("dev")
@Order(310)
@RequiredArgsConstructor
@Slf4j
public class PilotageDemoDataSeeder implements ApplicationRunner {

	private static final String SITE_PARIS = "DEMO-PARIS";
	private static final String SITE_LYON = "DEMO-LYON";
	private static final String PDV_PARIS_CAISSE = "DEMO-PDV-PARIS-CAISSE";
	private static final String PDV_PARIS_ORTHO = "DEMO-PDV-PARIS-ORTHO";
	private static final String PDV_LYON_CAISSE = "DEMO-PDV-LYON-CAISSE";

	/** Premier numéro du jeu étendu — s’il existe, tout le jeu est considéré comme déjà chargé. */
	private static final String FIRST_TICKET = "VTE-PIL-D-001";

	private final SiteRepository siteRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final UserRepo userRepo;
	private final ProductRepo productRepo;
	private final SellRepo sellRepo;
	private final SellService sellService;
	private final MouvementStockRepository mouvementStockRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (sellRepo.existsByNumber(FIRST_TICKET)) {
			log.info("s7 pilotage : jeu démo étendu déjà présent ({}) — ignoré.", FIRST_TICKET);
			return;
		}
		Site siteParis = siteRepository.findByCode(SITE_PARIS).orElse(null);
		if (siteParis == null) {
			log.warn("s7 pilotage : site {} absent — pas d’insertion.", SITE_PARIS);
			return;
		}
		PointDeVente pdvParis = pointDeVenteRepository.findByCode(PDV_PARIS_CAISSE).orElse(null);
		if (pdvParis == null) {
			log.warn("s7 pilotage : PDV {} absent — pas d’insertion.", PDV_PARIS_CAISSE);
			return;
		}
		PointDeVente pdvOrtho = pointDeVenteRepository.findByCode(PDV_PARIS_ORTHO).orElse(null);
		Site siteLyon = siteRepository.findByCode(SITE_LYON).orElse(null);
		PointDeVente pdvLyon = siteLyon == null
				? null
				: pointDeVenteRepository.findByCode(PDV_LYON_CAISSE).orElse(null);

		AppUser pharma = userRepo.findByLogin("pharma");
		if (pharma == null) {
			log.warn("s7 pilotage : utilisateur pharma introuvable — pas d’insertion.");
			return;
		}

		DevSeedProductPins.Trio demoProducts = DevSeedProductPins.resolveDemoProducts(productRepo);
		if (demoProducts == null) {
			log.warn("s7 pilotage : aucun produit en base — pas d’insertion.");
			return;
		}
		Product productPara = demoProducts.first();
		Product productIbu = demoProducts.second();
		Product productVit = demoProducts.third();

		int n = 0;
		// ——— 2025 : visible dans « années les plus actives » (plage 2025-01-01 → 2026-12-31) ———
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-001", LocalDate.of(2025, 10, 8), bd("82.50"),
				line(productIbu, 11, 0, bd("7.50")));
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-002", LocalDate.of(2025, 12, 18), bd("72.00"),
				line(productVit, 6, 0, bd("12.00")));

		// ——— 2026 : au moins une vente par mois (onglet « mois les plus actifs », année 2026) ———
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-003", LocalDate.of(2026, 1, 7), bd("54.60"),
				line(productPara, 7, 0, bd("7.80")));
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-004", LocalDate.of(2026, 1, 24), bd("60.00"),
				line(productIbu, 8, 0, bd("7.50")));

		if (pdvLyon != null && siteLyon != null) {
			n += save(pharma, siteLyon, pdvLyon, "VTE-PIL-D-005", LocalDate.of(2026, 2, 5), bd("90.00"),
					line(productIbu, 12, 0, bd("7.50")));
		}
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-006", LocalDate.of(2026, 2, 22), bd("48.00"),
				line(productVit, 4, 0, bd("12.00")));

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-007", LocalDate.of(2026, 3, 12), bd("62.40"),
				line(productPara, 8, 0, bd("7.80")));

		if (pdvOrtho != null) {
			n += save(pharma, siteParis, pdvOrtho, "VTE-PIL-D-008", LocalDate.of(2026, 4, 3), bd("46.80"),
					line(productPara, 6, 0, bd("7.80")));
		}
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-009", LocalDate.of(2026, 4, 19), bd("52.50"),
				line(productIbu, 7, 0, bd("7.50")));

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-010", LocalDate.of(2026, 5, 11), bd("96.00"),
				line(productVit, 8, 0, bd("12.00")));

		// Juin : pic d’activité (plusieurs tickets)
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-011", LocalDate.of(2026, 6, 4), bd("102.00"),
				line(productPara, 10, 0, bd("7.80")), line(productVit, 2, 0, bd("12.00")));
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-012", LocalDate.of(2026, 6, 17), bd("112.50"),
				line(productIbu, 15, 0, bd("7.50")));
		if (pdvLyon != null && siteLyon != null) {
			n += save(pharma, siteLyon, pdvLyon, "VTE-PIL-D-013", LocalDate.of(2026, 6, 28), bd("108.00"),
					line(productVit, 9, 0, bd("12.00")));
		}

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-014", LocalDate.of(2026, 7, 9), bd("75.00"),
				line(productIbu, 10, 0, bd("7.50")));

		if (pdvLyon != null && siteLyon != null) {
			n += save(pharma, siteLyon, pdvLyon, "VTE-PIL-D-015", LocalDate.of(2026, 8, 14), bd("93.60"),
					line(productPara, 12, 0, bd("7.80")));
		}
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-016", LocalDate.of(2026, 8, 27), bd("34.20"),
				line(productVit, 3, 5, bd("12.00")));

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-017", LocalDate.of(2026, 9, 6), bd("61.50"),
				line(productPara, 5, 0, bd("7.80")), line(productIbu, 3, 0, bd("7.50")));

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-018", LocalDate.of(2026, 10, 10), bd("82.50"),
				line(productIbu, 11, 0, bd("7.50")));
		if (pdvOrtho != null) {
			n += save(pharma, siteParis, pdvOrtho, "VTE-PIL-D-019", LocalDate.of(2026, 10, 25), bd("60.00"),
					line(productVit, 5, 0, bd("12.00")));
		}

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-020", LocalDate.of(2026, 11, 13), bd("70.20"),
				line(productPara, 9, 0, bd("7.80")));

		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-021", LocalDate.of(2026, 12, 5), bd("48.00"),
				line(productVit, 4, 0, bd("12.00")));
		n += save(pharma, siteParis, pdvParis, "VTE-PIL-D-022", LocalDate.of(2026, 12, 21), bd("37.50"),
				line(productIbu, 5, 0, bd("7.50")));

		log.info(
				"s7 pilotage : {} ventes démo insérées ({} …). Tous les mois 2026 couverts ; PDV Paris caisse{} ; Lyon={}.",
				n,
				FIRST_TICKET,
				pdvOrtho != null ? ", ortho" : "",
				pdvLyon != null ? "oui" : "non");
	}

	private int save(
			AppUser pharma,
			Site site,
			PointDeVente pdv,
			String number,
			LocalDate date,
			BigDecimal totalPrice,
			SellDetails... lines) {
		sellService.save(ticket(number, date, site, pdv, pharma, totalPrice, lines));
		return 1;
	}

	private static BigDecimal bd(String s) {
		return new BigDecimal(s);
	}

	private static Sell ticket(
			String number,
			LocalDate date,
			Site site,
			PointDeVente pdv,
			AppUser vendeur,
			BigDecimal totalPrice,
			SellDetails... lines) {
		Sell sell = Sell.builder()
				.number(number)
				.dateVente(date)
				.seller("Démo pilotage")
				.client("Client stats")
				.sellType("COMPTANT")
				.paymentMode("Mixte")
				.totalPrice(totalPrice)
				.amountReceived(totalPrice)
				.changeGiven(BigDecimal.ZERO)
				.remark("Données de test s7_pilotage (jeu étendu)")
				.site(site)
				.pointDeVente(pdv)
				.effectueePar(vendeur)
				.lignes(new ArrayList<>())
				.build();
		for (SellDetails line : lines) {
			line.setSell(sell);
			sell.getLignes().add(line);
		}
		return sell;
	}

	private SellDetails line(Product product, int qty, int discountPct, BigDecimal unitPrice) {
		var builder = SellDetails.builder()
				.product(product)
				.quantity(qty)
				.discount(discountPct)
				.price(unitPrice);
		LatestProductMovementSupport.latest(mouvementStockRepository, product).ifPresent(mv -> {
			builder.batch(mv.getBatch());
			builder.unitCostAtSale(mv.getCostPrice());
		});
		return builder.build();
	}
}
