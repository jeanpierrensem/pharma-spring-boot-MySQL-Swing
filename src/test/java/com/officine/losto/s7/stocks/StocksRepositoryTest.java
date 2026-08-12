package com.officine.losto.s7.stocks;

import com.officine.losto.entity.Batch;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Site;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class StocksRepositoryTest {

	@Autowired
	private StockCentralRepository stockCentralRepository;

	@Autowired
	private MouvementStockRepository mouvementStockRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void stockCentral_secondRowForSameMagasinProductAndBatch_violatesUniqueConstraint() {
		Site site = persistSite("S-STK-UK1", "Site stock");
		MagasinCentral mc = persistMagasin(site, "MC-UK", "Magasin UK");
		Product product = persistProduct("P-UK", "CB-UK-1");
		Batch batch = persistBatch("LOT-UK-1");
		persistStockCentral(site, mc, product, batch, 5);

		assertThatThrownBy(() -> {
			entityManager.persist(StockCentral.builder()
					.site(site)
					.magasinCentral(mc)
					.product(product)
					.batch(batch)
					.qteDisponible(1)
					.qteReservee(0)
					.updatedAt(LocalDateTime.now())
					.build());
			entityManager.flush();
		}).satisfies(ex -> assertConstraintOrDataIntegrity(ex));
	}

	@Test
	void stockCentral_twoRowsForSameProductDifferentBatch_allowed() {
		Site site = persistSite("S-STK-UK1B", "Site stock B");
		MagasinCentral mc = persistMagasin(site, "MC-UKB", "Magasin UK B");
		Product product = persistProduct("P-UK-B", "CB-UK-B");
		Batch batch1 = persistBatch("LOT-UK-B1");
		Batch batch2 = persistBatch("LOT-UK-B2");
		persistStockCentral(site, mc, product, batch1, 5);
		persistStockCentral(site, mc, product, batch2, 9);

		assertThat(stockCentralRepository.findByMagasinCentral_IdAndProduct_Id(mc.getId(), product.getId()))
				.hasSize(2);
	}

	@Test
	void stockPdv_secondRowForSamePdvAndProduct_violatesUniqueConstraint() {
		Site site = persistSite("S-STK-UK2", "Site PDV");
		PointDeVente pdv = persistPdv(site, "PDV-UK");
		Product product = persistProduct("P-UK2", "CB-UK-2");
		persistStockPdv(pdv, product, 3);

		assertThatThrownBy(() -> {
			entityManager.persist(StockPdv.builder()
					.pointDeVente(pdv)
					.product(product)
					.qteDisponible(1)
					.qteReservee(0)
					.updatedAt(LocalDateTime.now())
					.build());
			entityManager.flush();
		}).satisfies(ex -> assertConstraintOrDataIntegrity(ex));
	}

	@Test
	void mouvementStock_saveAndFindByProduct() {
		Product product = persistProduct("P-MVT", "CB-MVT");
		MouvementStock m = MouvementStock.builder()
				.product(product)
				.typeMouvement(TypeMouvementStock.ENTREE)
				.quantiteAlgebrique(12)
				.dateMouvement(LocalDateTime.now())
				.build();
		mouvementStockRepository.save(m);
		entityManager.flush();
		entityManager.clear();

		assertThat(mouvementStockRepository.findByProduct_IdOrderByDateMouvementDesc(product.getId()))
				.hasSize(1)
				.first()
				.satisfies(row -> {
					assertThat(row.getQuantiteAlgebrique()).isEqualTo(12);
					assertThat(row.getTypeMouvement()).isEqualTo(TypeMouvementStock.ENTREE);
				});
	}

	@Test
	void stockCentralRepository_findByMagasinAndProduct() {
		Site site = persistSite("S-STK-FIND", "Site find");
		MagasinCentral mc = persistMagasin(site, "MC-FIND", "Mag find");
		Product product = persistProduct("P-FIND", "CB-FIND");
		persistStockCentral(site, mc, product, persistBatch("LOT-FIND"), 7);

		assertThat(stockCentralRepository.findByMagasinCentral_IdAndProduct_Id(mc.getId(), product.getId()))
				.hasSize(1)
				.first()
				.satisfies(row -> assertThat(row.getQteDisponible()).isEqualTo(7));
	}

	@Test
	void mouvementStock_unknownProductId_rejectedByPersistenceLayer() {
		assertThatThrownBy(() -> {
			EntityManager em = entityManager.getEntityManager();
			Product phantom = em.getReference(Product.class, 9_999_999L);
			MouvementStock m = MouvementStock.builder()
					.product(phantom)
					.typeMouvement(TypeMouvementStock.SORTIE)
					.quantiteAlgebrique(-1)
					.dateMouvement(LocalDateTime.now())
					.build();
			entityManager.persist(m);
			entityManager.flush();
		}).satisfies(ex -> assertEntityNotFoundOrFkViolation(ex));
	}

	private void persistStockCentral(Site site, MagasinCentral mc, Product product, Batch batch, int qteDispo) {
		entityManager.persist(StockCentral.builder()
				.site(site)
				.magasinCentral(mc)
				.product(product)
				.batch(batch)
				.qteDisponible(qteDispo)
				.qteReservee(0)
				.updatedAt(LocalDateTime.now())
				.build());
		entityManager.flush();
	}

	private Batch persistBatch(String number) {
		Batch batch = Batch.builder().number(number).quantity(100).build();
		entityManager.persist(batch);
		entityManager.flush();
		return batch;
	}

	private void persistStockPdv(PointDeVente pdv, Product product, int qteDispo) {
		entityManager.persist(StockPdv.builder()
				.pointDeVente(pdv)
				.product(product)
				.qteDisponible(qteDispo)
				.qteReservee(0)
				.updatedAt(LocalDateTime.now())
				.build());
		entityManager.flush();
	}

	private Site persistSite(String code, String libelle) {
		Site site = Site.builder()
				.code(code)
				.libelle(libelle)
				.actif(true)
				.build();
		entityManager.persist(site);
		entityManager.flush();
		return site;
	}

	private MagasinCentral persistMagasin(Site site, String code, String libelle) {
		MagasinCentral mc = MagasinCentral.builder()
				.site(site)
				.code(code)
				.libelle(libelle)
				.build();
		entityManager.persist(mc);
		entityManager.flush();
		return mc;
	}

	private PointDeVente persistPdv(Site site, String code) {
		PointDeVente pdv = PointDeVente.builder()
				.site(site)
				.code(code)
				.libelle("Caisse")
				.actif(true)
				.build();
		entityManager.persist(pdv);
		entityManager.flush();
		return pdv;
	}

	private Product persistProduct(String name, String codeBar) {
		Product p = Product.builder()
				.name(name)
				.codeBar(codeBar)
				.build();
		entityManager.persist(p);
		entityManager.flush();
		return p;
	}

	private static void assertConstraintOrDataIntegrity(Throwable ex) {
		if (ex instanceof ConstraintViolationException || ex instanceof DataIntegrityViolationException) {
			return;
		}
		Throwable cause = ex.getCause();
		int depth = 0;
		while (cause != null && depth++ < 5) {
			if (cause instanceof ConstraintViolationException || cause instanceof DataIntegrityViolationException) {
				return;
			}
			cause = cause.getCause();
		}
		throw new AssertionError("Expected constraint / data integrity in cause chain", ex);
	}

	private static void assertEntityNotFoundOrFkViolation(Throwable ex) {
		Throwable t = ex;
		for (int i = 0; i < 8 && t != null; i++) {
			if (t instanceof DataIntegrityViolationException || t instanceof ConstraintViolationException) {
				return;
			}
			if (t instanceof jakarta.persistence.EntityNotFoundException) {
				return;
			}
			if (t.getClass().getSimpleName().contains("EntityNotFound")) {
				return;
			}
			t = t.getCause();
		}
		throw new AssertionError("Expected FK / entity-not-found failure", ex);
	}
}
