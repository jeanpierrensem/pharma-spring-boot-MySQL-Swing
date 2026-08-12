package com.officine.losto.s4.ventes.bootstrap;

import com.officine.losto.config.DevSeedProductPins;
import com.officine.losto.entity.AffectationVendeur;
import com.officine.losto.entity.AppUser;
import com.officine.losto.entity.Batch;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.LigneBonCommandeInterne;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.MouvementStock;
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
import com.officine.losto.s5.reappro.repository.AffectationVendeurRepository;
import com.officine.losto.s5.reappro.repository.BonCommandeInterneRepository;
import com.officine.losto.s5.reappro.service.AffectationVendeurServiceImpl;
import com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl;
import com.officine.losto.service.SellService;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.util.LatestProductMovementSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Données de test pour {@code SELL} / {@code SELL_DETAILS} et pour le s5
 * ({@code BON_COMMANDE_INTERNE} / {@code LIGNE_BON_COMMANDE_INTERNE} / {@code AFFECTATION_VENDEUR}).
 * S’exécute après {@link com.officine.losto.s1.organisation.bootstrap.OrganisationDemoDataSeeder} (profil
 * {@code dev}), uniquement si le jeu de démo organisation existe (code site {@code DEMO-PARIS}).
 */
@Component
@Profile("dev")
@Order(300)
@RequiredArgsConstructor
@Slf4j
public class VentesS5DemoDataSeeder implements ApplicationRunner {

	private static final String SITE_PARIS = "DEMO-PARIS";
	private static final String PDV_PARIS_CAISSE = "DEMO-PDV-PARIS-CAISSE";
	private static final String PDV_LYON_CAISSE = "DEMO-PDV-LYON-CAISSE";
	private static final String SELL1 = "VTE-2026-0001";
	private static final String SELL2 = "VTE-2026-0002";
	private static final String BON1 = "BINT-DEMO-2026-001";
	private static final String BON2 = "BINT-DEMO-2026-002";

	private final SiteRepository siteRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final MagasinCentralRepository magasinCentralRepository;
	private final UserRepo userRepo;
	private final ProductRepo productRepo;
	private final SellRepo sellRepo;
	private final SellService sellService;
	private final BonCommandeInterneRepository bonCommandeInterneRepository;
	private final BonCommandeInterneServiceImpl bonCommandeInterneService;
	private final AffectationVendeurRepository affectationVendeurRepository;
	private final AffectationVendeurServiceImpl affectationVendeurService;
	private final MouvementStockRepository mouvementStockRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		boolean haveSells = sellRepo.existsByNumber(SELL1);
		boolean haveBons = bonCommandeInterneRepository.existsByNumber(BON1);
		if (haveSells && haveBons && affectationVendeurRepository.count() > 0) {
			log.info("Ventes S4/S5 : données de démo déjà présentes — ignoré.");
			return;
		}
		Site siteParis = siteRepository.findByCode(SITE_PARIS).orElse(null);
		if (siteParis == null) {
			log.warn(
					"Ventes S4/S5 : site {} absent (seed organisation requis) — pas d’insertion ventes/bons/affectations.",
					SITE_PARIS);
			return;
		}
		Site siteLyon = siteRepository.findByCode("DEMO-LYON").orElse(null);
		PointDeVente pdvParis = pointDeVenteRepository.findByCode(PDV_PARIS_CAISSE).orElse(null);
		PointDeVente pdvLyon = siteLyon == null
				? null
				: pointDeVenteRepository.findByCode(PDV_LYON_CAISSE).orElse(null);
		MagasinCentral magParis = magasinCentralRepository.findBySite_Id(siteParis.getId()).orElse(null);
		MagasinCentral magLyon = siteLyon == null
				? null
				: magasinCentralRepository.findBySite_Id(siteLyon.getId()).orElse(null);

		if (pdvParis == null || magParis == null) {
			log.warn("Ventes S4/S5 : PDV ou magasin central Paris manquant — abandon.");
			return;
		}

		AppUser pharma = userRepo.findByLogin("pharma");
		AppUser admin = userRepo.findByLogin("admin");
		if (pharma == null) {
			log.warn("Ventes S4/S5 : utilisateur login=pharma introuvable — abandon.");
			return;
		}

		DevSeedProductPins.Trio demoProducts = DevSeedProductPins.resolveDemoProducts(productRepo);
		if (demoProducts == null) {
			log.warn("Ventes S4/S5 : aucun produit en base — abandon.");
			return;
		}
		Product productPara = demoProducts.first();
		Product productIbu = demoProducts.second();
		Product productVit = demoProducts.third();

		if (!haveSells) {
			seedSells(pdvParis, siteParis, pharma, productPara, productIbu, productVit);
		}
		if (!haveBons) {
			seedBons(siteParis, siteLyon, pdvParis, pdvLyon, magParis, magLyon, pharma, productPara, productIbu, productVit);
		}
		if (affectationVendeurRepository.count() == 0) {
			seedAffectations(pdvParis, admin, pharma);
		}
		log.info("Ventes S4/S5 : jeux de démo vérifiés / complétés (ventes, bons, affectations).");
	}

	private void seedSells(
			PointDeVente pdvParis,
			Site siteParis,
			AppUser pharma,
			Product productPara,
			Product productIbu,
			Product productVit) {
		Sell sell1 = Sell.builder()
				.number(SELL1)
				.dateVente(LocalDate.of(2026, 4, 1))
				.seller("Marie Pharmacien")
				.client("Client comptoir")
				.sellType("COMPTANT")
				.paymentMode("Espèces")
				.totalPrice(new BigDecimal("23.40"))
				.amountReceived(new BigDecimal("25.00"))
				.changeGiven(new BigDecimal("1.60"))
				.remark("Exemple de vente comptant")
				.site(siteParis)
				.pointDeVente(pdvParis)
				.effectueePar(pharma)
				.lignes(new ArrayList<>())
				.build();
		sell1.getLignes()
				.add(
						SellDetails.builder()
								.sell(sell1)
								.product(productPara)
								.quantity(2)
								.discount(0)
								.price(new BigDecimal("7.80"))
								.batch(latestBatch(productPara).orElse(null))
								.unitCostAtSale(latestCost(productPara).orElse(null))
								.build());
		sell1.getLignes()
				.add(
						SellDetails.builder()
								.sell(sell1)
								.product(productVit)
								.quantity(1)
								.discount(5)
								.price(new BigDecimal("11.40"))
								.batch(latestBatch(productVit).orElse(null))
								.unitCostAtSale(latestCost(productVit).orElse(null))
								.build());
		sellService.save(sell1);

		Sell sell2 = Sell.builder()
				.number(SELL2)
				.dateVente(LocalDate.of(2026, 4, 2))
				.seller("Marie Pharmacien")
				.client("Dr Martin")
				.sellType("ORDONNANCE")
				.paymentMode("Carte")
				.totalPrice(new BigDecimal("15.00"))
				.amountReceived(new BigDecimal("15.00"))
				.changeGiven(BigDecimal.ZERO)
				.remark("Ordonnance scannée")
				.site(siteParis)
				.pointDeVente(pdvParis)
				.effectueePar(pharma)
				.lignes(new ArrayList<>())
				.build();
		sell2.getLignes()
				.add(
						SellDetails.builder()
								.sell(sell2)
								.product(productIbu)
								.quantity(2)
								.discount(0)
								.price(new BigDecimal("7.50"))
								.batch(latestBatch(productIbu).orElse(null))
								.unitCostAtSale(latestCost(productIbu).orElse(null))
								.build());
		sellService.save(sell2);
	}

	private void seedBons(
			Site siteParis,
			Site siteLyon,
			PointDeVente pdvParis,
			PointDeVente pdvLyon,
			MagasinCentral magParis,
			MagasinCentral magLyon,
			AppUser pharma,
			Product productPara,
			Product productIbu,
			Product productVit) {
		BonCommandeInterne b1 = BonCommandeInterne.builder()
				.number(BON1)
				.orderDate(LocalDate.of(2026, 4, 3))
				.commentaire("Réassort test — caisse → magasin central Paris")
				.site(siteParis)
				.pointDeVente(pdvParis)
				.user(pharma)
				.magasinCentral(magParis)
				.lignes(new ArrayList<>())
				.build();
		b1.getLignes()
				.add(
						LigneBonCommandeInterne.builder()
								.bon(b1)
								.product(productPara)
								.quantity(24)
								.unitPrice(new BigDecimal("2.15"))
								.build());
		b1.getLignes()
				.add(
						LigneBonCommandeInterne.builder()
								.bon(b1)
								.product(productIbu)
								.quantity(10)
								.unitPrice(new BigDecimal("4.20"))
								.build());
		bonCommandeInterneService.save(b1);
		b1.setStatut(StatutBonCommandeInterne.ENVOYE);
		bonCommandeInterneService.save(b1, StatutBonCommandeInterne.BROUILLON);

		if (siteLyon != null && pdvLyon != null && magLyon != null) {
			BonCommandeInterne b2 = BonCommandeInterne.builder()
					.number(BON2)
					.orderDate(LocalDate.of(2026, 4, 4))
					.commentaire("Bon test Lyon")
					.site(siteLyon)
					.pointDeVente(pdvLyon)
					.user(pharma)
					.magasinCentral(magLyon)
					.lignes(new ArrayList<>())
					.build();
			b2.getLignes()
					.add(
							LigneBonCommandeInterne.builder()
									.bon(b2)
									.product(productVit)
									.quantity(18)
									.unitPrice(new BigDecimal("6.80"))
									.build());
			bonCommandeInterneService.save(b2);
			b2.setStatut(StatutBonCommandeInterne.ENVOYE);
			bonCommandeInterneService.save(b2, StatutBonCommandeInterne.BROUILLON);
			b2.setStatut(StatutBonCommandeInterne.TRAITE);
			bonCommandeInterneService.save(b2, StatutBonCommandeInterne.ENVOYE);
		}
	}

	private void seedAffectations(PointDeVente pdvParis, AppUser admin, AppUser pharma) {
		affectationVendeurService.save(
				AffectationVendeur.builder()
						.debut(LocalDateTime.of(2026, 4, 1, 8, 0))
						.fin(LocalDateTime.of(2026, 4, 1, 12, 30))
						.actifCreneau(true)
						.appUser(pharma)
						.pointDeVente(pdvParis)
						.build());
		if (admin != null) {
			affectationVendeurService.save(
					AffectationVendeur.builder()
							.debut(LocalDateTime.of(2026, 4, 2, 14, 0))
							.fin(LocalDateTime.of(2026, 4, 2, 18, 0))
							.actifCreneau(true)
							.appUser(admin)
							.pointDeVente(pdvParis)
							.build());
		}
		pointDeVenteRepository
				.findByCode("DEMO-PDV-PARIS-ORTHO")
				.ifPresent(
						ortho -> affectationVendeurService.save(
								AffectationVendeur.builder()
										.debut(LocalDateTime.of(2026, 4, 5, 9, 0))
										.fin(LocalDateTime.of(2026, 4, 5, 13, 0))
										.actifCreneau(false)
										.appUser(pharma)
										.pointDeVente(ortho)
										.build()));
	}

	private Optional<Batch> latestBatch(Product product) {
		return LatestProductMovementSupport.latest(mouvementStockRepository, product)
				.map(MouvementStock::getBatch);
	}

	private Optional<BigDecimal> latestCost(Product product) {
		return LatestProductMovementSupport.latest(mouvementStockRepository, product)
				.map(MouvementStock::getCostPrice);
	}
}
