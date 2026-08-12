package com.officine.losto.s7.pilotage;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.officine.losto.entity.Batch;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Provider;
import com.officine.losto.entity.Sell;
import com.officine.losto.entity.SellDetails;
import com.officine.losto.entity.Site;
import com.officine.losto.s7.pilotage.dto.PeriodeFiltre;
import com.officine.losto.s7.pilotage.dto.ResultatCaParPdv;
import com.officine.losto.s7.pilotage.dto.ResultatMargeProduitLot;

@DataJpaTest
@ActiveProfiles("test")
@Import(PilotageVenteServiceImpl.class)
class PilotageVenteServiceImplTest {

	@Autowired
	private PilotageVenteService pilotageVenteService;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void caParPointDeVente_and_marges_useDiscountAndUnitCost() {
		Site site = Site.builder().code("S-PIL").libelle("Site pilotage").actif(true).build();
		entityManager.persist(site);
		PointDeVente pdv = PointDeVente.builder().site(site).code("PDV-PIL").libelle("Caisse test").actif(true).build();
		entityManager.persist(pdv);
		Provider provider = Provider.builder().code("PR-PIL").designation("Fournisseur").build();
		entityManager.persist(provider);
		Batch batch = Batch.builder().provider(provider).number("LOT-PIL-1").quantity(100).build();
		entityManager.persist(batch);
		Product product = Product.builder()
				.name("Med PIL")
				.codeBar("CB-PIL-1")
				.build();
		entityManager.persist(product);

		Sell sell = Sell.builder()
				.dateVente(LocalDate.of(2026, 6, 15))
				.site(site)
				.pointDeVente(pdv)
				.totalPrice(new BigDecimal("23.00"))
				.lignes(new ArrayList<>())
				.build();
		entityManager.persist(sell);

		SellDetails line1 = SellDetails.builder()
				.sell(sell)
				.product(product)
				.quantity(2)
				.discount(0)
				.price(new BigDecimal("10.00"))
				.batch(batch)
				.unitCostAtSale(new BigDecimal("4.00"))
				.build();
		SellDetails line2 = SellDetails.builder()
				.sell(sell)
				.product(product)
				.quantity(1)
				.discount(10)
				.price(new BigDecimal("10.00"))
				.batch(batch)
				.unitCostAtSale(new BigDecimal("4.00"))
				.build();
		entityManager.persist(line1);
		entityManager.persist(line2);
		entityManager.flush();

		PeriodeFiltre periode = new PeriodeFiltre(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30), null);

		List<ResultatCaParPdv> ca = pilotageVenteService.caParPointDeVente(periode);
		assertThat(ca).hasSize(1);
		assertThat(ca.getFirst().getPointDeVenteId()).isEqualTo(pdv.getId());
		assertThat(ca.getFirst().getChiffreAffaires()).isEqualByComparingTo("29.00");

		List<ResultatMargeProduitLot> marges = pilotageVenteService.margesParProduitEtLot(periode);
		assertThat(marges).hasSize(1);
		ResultatMargeProduitLot m = marges.getFirst();
		assertThat(m.getQuantiteVendue()).isEqualTo(3L);
		assertThat(m.getChiffreAffaires()).isEqualByComparingTo("29.00");
		assertThat(m.getCoutRevient()).isEqualByComparingTo("12.00");
		assertThat(m.getMarge()).isEqualByComparingTo("17.00");
	}

	@Test
	void caParPointDeVente_invalidPeriod_returnsEmpty() {
		PeriodeFiltre bad = new PeriodeFiltre(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 6, 1), null);
		assertThat(pilotageVenteService.caParPointDeVente(bad)).isEmpty();
	}
}
