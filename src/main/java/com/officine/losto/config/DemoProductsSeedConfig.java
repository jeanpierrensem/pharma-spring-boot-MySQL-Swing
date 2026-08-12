package com.officine.losto.config;

import com.officine.losto.entity.Batch;
import com.officine.losto.entity.Category;
import com.officine.losto.entity.DrugType;
import com.officine.losto.entity.Form;
import com.officine.losto.entity.Packaging;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Section;
import com.officine.losto.entity.Threshold;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.model.BatchRepo;
import com.officine.losto.model.CategoryRepo;
import com.officine.losto.model.DrugTypeRepo;
import com.officine.losto.model.FormRepo;
import com.officine.losto.model.PackagingRepo;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.SectionRepo;
import com.officine.losto.model.ThresholdRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Complète le catalogue avec des produits de démo jusqu'à un seuil (par défaut 100), profil {@code dev} uniquement.
 * S'exécute après le seed principal {@link com.officine.losto.OfficineApplication}.
 */
@Configuration
@Profile("dev")
public class DemoProductsSeedConfig {

	@Bean
	@Order(2)
	CommandLineRunner seedDemoProductsToThreshold(
			ProductRepo productRepo,
			CategoryRepo categoryRepo,
			FormRepo formRepo,
			DrugTypeRepo drugTypeRepo,
			SectionRepo sectionRepo,
			PackagingRepo packagingRepo,
			BatchRepo batchRepo,
			ThresholdRepo thresholdRepo,
			MouvementStockRepository mouvementStockRepository,
			@Value("${officine.seed.demo-products-target:100}") int targetCount) {

		return args -> {
			if (targetCount <= 0) {
				return;
			}
			long existing = productRepo.count();
			if (existing >= targetCount) {
				return;
			}
			int missing = (int) (targetCount - existing);
			List<Category> cats = categoryRepo.findAll();
			List<Form> forms = formRepo.findAll();
			List<DrugType> drugTypes = drugTypeRepo.findAll();
			List<Section> sections = sectionRepo.findAll();
			List<Packaging> packs = packagingRepo.findAll();
			List<Batch> batches = batchRepo.findAll();
			List<Threshold> thresholds = thresholdRepo.findAll();

			ThreadLocalRandom rnd = ThreadLocalRandom.current();
			List<Product> toSave = new ArrayList<>(missing);
			List<MouvementStock> initialMovements = new ArrayList<>(missing);
			for (int i = 0; i < missing; i++) {
				int ordinal = (int) existing + i + 1;
				BigDecimal cost = BigDecimal.valueOf(rnd.nextDouble(0.5, 28.0)).setScale(2, RoundingMode.HALF_UP);
				BigDecimal sell = cost.multiply(BigDecimal.valueOf(1.12 + rnd.nextDouble(0.08, 0.55)))
						.setScale(2, RoundingMode.HALF_UP);
				int qty = rnd.nextInt(1, 501);
				Batch batch = batches.isEmpty() ? null : batches.get(ordinal % batches.size());

				var builder = Product.builder()
						.name("Produit démo " + String.format("%03d", ordinal))
						.codeBar("3770999" + String.format("%05d", ordinal))
						.dosage((50 + rnd.nextInt(450)) + " mg");
				if (!forms.isEmpty()) {
					builder.form(forms.get(ordinal % forms.size()));
				}
				if (!drugTypes.isEmpty()) {
					builder.drugType(drugTypes.get(ordinal % drugTypes.size()));
				}
				if (!cats.isEmpty()) {
					builder.category(cats.get(ordinal % cats.size()));
				}
				if (!sections.isEmpty()) {
					builder.section(sections.get(ordinal % sections.size()));
				}
				if (!packs.isEmpty()) {
					builder.packaging(packs.get(ordinal % packs.size()));
				}
				if (!thresholds.isEmpty()) {
					builder.thresholds(new HashSet<>(Collections.singletonList(
							thresholds.get(ordinal % thresholds.size()))));
				}
				Product product = builder.build();
				toSave.add(product);
				initialMovements.add(MouvementStock.builder()
						.product(product)
						.batch(batch)
						.typeMouvement(TypeMouvementStock.ENTREE)
						.quantiteAlgebrique(qty)
						.costPrice(cost)
						.sellPrice(sell)
						.dateMouvement(LocalDateTime.now())
						.commentaire("Seed démo — entrée initiale")
						.build());
			}
			List<Product> saved = productRepo.saveAll(toSave);
			for (int i = 0; i < saved.size(); i++) {
				initialMovements.get(i).setProduct(saved.get(i));
			}
			mouvementStockRepository.saveAll(initialMovements);
		};
	}

	/**
	 * Renseigne un seuil critique (rotation sur le référentiel) pour les produits démo déjà en base
	 * (code-barres {@code 3770999…}) dont le seuil est encore vide — utile après ajout de la colonne FK.
	 */
	@Bean
	@Order(3)
	CommandLineRunner backfillDemoProductThresholds(ProductRepo productRepo, ThresholdRepo thresholdRepo) {
		return args -> {
			List<Threshold> thresholds = thresholdRepo.findAll();
			if (thresholds.isEmpty()) {
				return;
			}
			List<Product> without = productRepo.findAll().stream()
					.filter(p -> p.getCodeBar() != null && p.getCodeBar().startsWith("3770999"))
					.filter(p -> p.getThresholds() == null || p.getThresholds().isEmpty())
					.collect(Collectors.toList());
			if (without.isEmpty()) {
				return;
			}
			for (int i = 0; i < without.size(); i++) {
				without.get(i).getThresholds().add(thresholds.get(i % thresholds.size()));
			}
			productRepo.saveAll(without);
		};
	}
}
