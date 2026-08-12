package com.officine.losto;

import com.officine.losto.catalog.*;
import com.officine.losto.config.*;
import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import com.officine.losto.s7.stocks.repository.*;
import com.officine.losto.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.core.io.*;
import org.springframework.security.crypto.password.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;


@SpringBootApplication
@EnableConfigurationProperties({UserPhotosProperties.class, ProductPhotosProperties.class})
public class OfficineApplication {
	private static final String DEV_PASSWORD = "secret123";
	@Autowired
	ApplicationContext springContext;
	@Autowired
	private ResourceLoader resourceLoader;
	@Value("${officine.seed.products-csv:classpath:seed/ToLoad.csv}")
	private String seedProductsCsv;

	public static void main(String... args) {
		SpringApplication.run(OfficineApplication.class, args);
	}

	/**
	 * Tous les nœuds MENU dont le {@code pathCode} est la racine ou un descendant (onglets + actions inclus).
	 */
	private static List<Menu> menusForModuleSubtrees(Map<String, Menu> byPath, String... rootPaths) {
		Set<Menu> set = new LinkedHashSet<>();
		for (String r : rootPaths) {
			Menu root = byPath.get(r);
			if (root != null) {
				set.add(root);
			}
			String prefix = r + ".";
			for (Menu m : byPath.values()) {
				if (m.getPathCode() != null && m.getPathCode().startsWith(prefix)) {
					set.add(m);
				}
			}
		}
		return new ArrayList<>(set);
	}

	@Bean
	@Order(1)
	@Profile("dev")
	CommandLineRunner run() {
		return args -> populate();
	}

	private void populate() {
		GroupRepo groupRepo = springContext.getBean(GroupRepo.class);
		if (groupRepo.count() > 0) {
			return;
		}

		GroupService groupService = springContext.getBean(GroupService.class);
		UserService userService = springContext.getBean(UserService.class);
		ThresholdService thresholdService = springContext.getBean(ThresholdService.class);
		CategoryService categoryService = springContext.getBean(CategoryService.class);
		DrugTypeService drugTypeService = springContext.getBean(DrugTypeService.class);
		SectionService sectionService = springContext.getBean(SectionService.class);
		PackagingService packagingService = springContext.getBean(PackagingService.class);
		ProviderService providerService = springContext.getBean(ProviderService.class);
		FormService formService = springContext.getBean(FormService.class);
		BatchService batchService = springContext.getBean(BatchService.class);
		ProductService productService = springContext.getBean(ProductService.class);
		OrdersService ordersService = springContext.getBean(OrdersService.class);
		OrderDetailsService orderDetailsService = springContext.getBean(OrderDetailsService.class);
		ReceiptDetailsService receiptDetailsService = springContext.getBean(ReceiptDetailsService.class);
		MenuCatalogSyncService menuCatalogSyncService = springContext.getBean(MenuCatalogSyncService.class);
		MenuRepo menuRepo = springContext.getBean(MenuRepo.class);
		menuCatalogSyncService.syncFromCatalog();
		List<Menu> allMenus = menuRepo.findAll();
		Map<String, Menu> menuByPath = allMenus.stream()
				.filter(m -> m.getPathCode() != null)
				.collect(Collectors.toMap(Menu::getPathCode, m -> m, (a, b) -> a));

		AppGroup administrators = AppGroup.builder()
				.name("Administrators")
				.description("Gestion complète de l'officine")
				.selected(false)
				.build();
		AppGroup pharmacists = AppGroup.builder()
				.name("Pharmaciens")
				.description("Gestion Sécurité (habilitations, utilisateurs) — données dev")
				.selected(false)
				.build();
		AppGroup consultants = AppGroup.builder()
				.name("Consultants")
				.description("Gestion Sécurité (habilitations, utilisateurs) — données dev")
				.selected(false)
				.build();
		administrators.getMenus().addAll(allMenus);
		pharmacists.getMenus().addAll(menusForModuleSubtrees(menuByPath,
				MenuSecurityCatalog.NavRoots.SALES,
				MenuSecurityCatalog.NavRoots.ORGANISATION,
				MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
				MenuSecurityCatalog.NavRoots.PILOTAGE));
		consultants.getMenus().addAll(menusForModuleSubtrees(menuByPath, MenuSecurityCatalog.NavRoots.DASHBOARD));
		groupService.saveAll(Arrays.asList(administrators, pharmacists, consultants));

		PasswordEncoder passwordEncoder = springContext.getBean(PasswordEncoder.class);
		String encodedPassword = passwordEncoder.encode(DEV_PASSWORD);
		Instant now = Instant.now();

		AppUser adminUser = AppUser.builder()
				.login("admin")
				.password(encodedPassword)
				.phoneNumber("0601000001")
				.name("Admin User")
				.email("admin@officine.dev")
				.group(administrators)
				.enabled(true)
				.compteActif(true)
				.createdAt(now)
				.build();
		AppUser pharmaUser = AppUser.builder()
				.login("pharma")
				.password(encodedPassword)
				.phoneNumber("0601000002")
				.name("Marie Pharmacien")
				.email("marie@officine.dev")
				.group(pharmacists)
				.enabled(true)
				.compteActif(true)
				.createdAt(now)
				.build();
		AppUser consultUser = AppUser.builder()
				.login("consult")
				.password(encodedPassword)
				.phoneNumber("0601000003")
				.name("Jean Consultant")
				.email("jean@officine.dev")
				.group(consultants)
				.enabled(true)
				.compteActif(true)
				.createdAt(now)
				.build();
		userService.saveAll(Arrays.asList(adminUser, pharmaUser, consultUser));

		List<Threshold> thresholds = Arrays.asList(
				Threshold.builder().code("THR-STOCK-BAS").level(10).description("Alerte stock bas (quantité)").colorHex("#F9D204").build(),
				Threshold.builder().code("THR-PEREMPTION").level(90).description("Jours avant péremption (alerte)").colorHex("#F11D1C").build(),
				Threshold.builder().code("THR-VENTES").level(500).description("Seuil de caisse journalière (FCFA)").colorHex("#421799").build());
		thresholdService.saveAll(thresholds);

		List<Category> categories = Arrays.asList(
				Category.builder().code("ANTAL").description("Antalgiques").build(),
				Category.builder().code("CARDIO").description("Cardiologie").build(),
				Category.builder().code("DIG").description("Digestif").build(),
				Category.builder().code("VIT").description("Vitamines & compléments").build());
		categoryService.saveAll(categories);

		List<DrugType> drugTypes = Arrays.asList(
				DrugType.builder().code("DCI-PARA").description("Paracétamol (DCI)").build(),
				DrugType.builder().code("DCI-IBU").description("Ibuprofène (DCI)").build(),
				DrugType.builder().code("DCI-ASP").description("Acide acétylsalicylique").build());
		drugTypeService.saveAll(drugTypes);

		List<Section> sections = Arrays.asList(
				Section.builder().code("RAY-OTC").description("Rayon libre-service").build(),
				Section.builder().code("RAY-ORD").description("Rayon médicaments sur ordonnance").build(),
				Section.builder().code("RAY-PARA").description("Parapharmacie").build());
		sectionService.saveAll(sections);

		List<Packaging> packagings = Arrays.asList(
				Packaging.builder().code("BLI-16").description("Blister 16 comprimés").build(),
				Packaging.builder().code("FL-100").description("Flacon 100 ml").build(),
				Packaging.builder().code("BOI-30").description("Boîte 30 gélules").build());
		packagingService.saveAll(packagings);

		List<Provider> providers = Arrays.asList(
				Provider.builder().code("FOU-LAB1").designation("Laboratoires Centraux")
						.address("12 avenue Pasteur, Paris").phoneNumber("0142000000").email("contact@labcent.fr").build(),
				Provider.builder().code("FOU-GRO1").designation("Grossiste République")
						.address("5 rue de la République, Lyon").phoneNumber("0472000000").email("cmd@grossiste-rep.fr").build(),
				Provider.builder().code("FOU-IMP").designation("Import Médical SA")
						.address("Zone industrielle Nord").phoneNumber("0388000000").email("orders@import-medical.eu").build());
		providerService.saveAll(providers);
		Provider provider1 = providers.get(0);
		Provider provider2 = providers.get(1);

		List<Form> forms = Arrays.asList(
				Form.builder().code("COMP").description("Comprimé").build(),
				Form.builder().code("GEL").description("Gélule").build(),
				Form.builder().code("SIR").description("Sirop").build(),
				Form.builder().code("INJ").description("Injectable").build());
		formService.saveAll(forms);

		ZoneId z = ZoneId.systemDefault();
		LocalDate batchExpiryFar = LocalDate.of(2027, 6, 30);
		LocalDate batchExpiryNear = LocalDate.of(2026, 4, 15);

		List<Batch> batches = Arrays.asList(
				Batch.builder().number("LOT-PARA-2026-A").expiredDate(batchExpiryFar).quantity(500).provider(provider1).build(),
				Batch.builder().number("LOT-IBU-2026-B").expiredDate(batchExpiryNear).quantity(200).provider(provider1).build(),
				Batch.builder().number("LOT-VIT-2025-C").expiredDate(batchExpiryFar).quantity(120).provider(provider2).build());
		batchService.saveAll(batches);

		FormRepo formRepo = springContext.getBean(FormRepo.class);
		PackagingRepo packagingRepoBean = springContext.getBean(PackagingRepo.class);
		Resource productsCsv = resourceLoader.getResource(seedProductsCsv);
		if (!productsCsv.exists()) {
			throw new IllegalStateException("Fichier CSV produits introuvable: " + seedProductsCsv);
		}
		List<ProductCsvSeedSupport.ProductSeedRow> productRows;
		try {
			productRows = ProductCsvSeedSupport.loadProductSeedRows(productsCsv, formRepo, packagingRepoBean);
		} catch (IOException e) {
			throw new IllegalStateException("Lecture du CSV produits impossible : " + seedProductsCsv, e);
		}
		if (productRows.isEmpty()) {
			throw new IllegalStateException("Aucune ligne produit dans le CSV : " + seedProductsCsv);
		}
		List<Product> savedProducts = productService.saveAll(
				productRows.stream().map(ProductCsvSeedSupport.ProductSeedRow::product).toList());
		MouvementStockRepository mouvementStockRepository =
				springContext.getBean(com.officine.losto.s7.stocks.repository.MouvementStockRepository.class);
		List<MouvementStock> csvMovements = new ArrayList<>();
		for (int i = 0; i < savedProducts.size(); i++) {
			ProductCsvSeedSupport.ProductSeedRow row = productRows.get(i);
			Product saved = savedProducts.get(i);
			Integer qty = row.movementQuantity();
			if (qty == null || qty <= 0) {
				continue;
			}
			csvMovements.add(
					MouvementStock.builder()
							.product(saved)
							.typeMouvement(com.officine.losto.s7.stocks.domain.TypeMouvementStock.ENTREE)
							.quantiteAlgebrique(qty)
							.costPrice(row.costPrice())
							.sellPrice(row.sellPrice())
							.dateMouvement(java.time.LocalDateTime.now())
							.commentaire("Seed CSV — entrée initiale")
							.build());
		}
		if (!csvMovements.isEmpty()) {
			mouvementStockRepository.saveAll(csvMovements);
		}
		Product productPara = savedProducts.get(0);
		Product productIbu = savedProducts.size() > 1 ? savedProducts.get(1) : productPara;
		Product productVit = savedProducts.size() > 2 ? savedProducts.get(2) : productPara;

		Orders orderOpen = Orders.builder().user(adminUser).provider(provider1).number("CMD-2026-001")
				.orderDate(LocalDate.of(2026, 1, 8))
				.statut(Statut.NON).description("Commande ouverte — en attente de livraison").build();
		Orders orderPartial = Orders.builder().user(pharmaUser).provider(provider2).number("CMD-2026-002")
				.orderDate(LocalDate.of(2026, 2, 3))
				.statut(Statut.PARTIELLE).description("Livraison partielle").build();
		Orders orderDone = Orders.builder().user(adminUser).provider(provider1).number("CMD-2026-003")
				.orderDate(LocalDate.of(2026, 3, 20))
				.statut(Statut.COMPLETE).description("Commande réceptionnée intégralement").build();
		ordersService.saveAll(Arrays.asList(orderOpen, orderPartial, orderDone));

		OrdersDetails lineOpen1 = OrdersDetails.builder().orders(orderOpen).product(productPara).quantity(100).unitPrice(215)
				.discount(0).totalPrice(21500).build();
		OrdersDetails lineOpen2 = OrdersDetails.builder().orders(orderOpen).product(productIbu).quantity(40).unitPrice(420)
				.discount(5).totalPrice(15960).build();
		OrdersDetails linePart1 = OrdersDetails.builder().orders(orderPartial).product(productVit).quantity(30).unitPrice(680)
				.discount(0).totalPrice(20400).build();
		OrdersDetails linePart2 = OrdersDetails.builder().orders(orderPartial).product(productPara).quantity(50).unitPrice(215)
				.discount(10).totalPrice(9675).build();
		OrdersDetails lineDone1 = OrdersDetails.builder().orders(orderDone).product(productIbu).quantity(20).unitPrice(420)
				.discount(0).totalPrice(8400).build();
		List<OrdersDetails> allLines = Arrays.asList(lineOpen1, lineOpen2, linePart1, linePart2, lineDone1);
		orderDetailsService.saveAll(allLines);

		ReceiptDetails receipt1 = ReceiptDetails.builder().user(pharmaUser).ordersDetails(linePart1).receivedQuantity(30)
				.missingQuantity(0)
				.date(LocalDate.of(2026, 2, 10).atStartOfDay(z).toLocalDateTime())
				.observation("Réception conforme — palette A12").build();
		ReceiptDetails receipt2 = ReceiptDetails.builder().user(adminUser).ordersDetails(linePart2).receivedQuantity(20)
				.missingQuantity(30)
				.date(LocalDate.of(2026, 2, 11).atStartOfDay(z).toLocalDateTime())
				.observation("Manquant signalé au fournisseur").build();
		receiptDetailsService.saveAll(Arrays.asList(receipt1, receipt2));
	}

}