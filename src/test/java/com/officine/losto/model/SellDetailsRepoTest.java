package com.officine.losto.model;

import com.officine.losto.entity.Product;
import com.officine.losto.entity.Sell;
import com.officine.losto.entity.SellDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SellDetailsRepoTest {

	@Autowired
	private SellDetailsRepo sellDetailsRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findBySell_returnsOnlyMatchingDetails() {
		Sell sell1 = entityManager.persist(Sell.builder().number("S-1").dateVente(LocalDate.of(2026, 1, 10)).build());
		Sell sell2 = entityManager.persist(Sell.builder().number("S-2").dateVente(LocalDate.of(2026, 1, 11)).build());
		Product product = entityManager.persist(Product.builder()
				.name("Vitamin C")
				.codeBar("VIT-C")
				.build());
		entityManager.persist(SellDetails.builder().sell(sell1).product(product).quantity(1).discount(0).price(new BigDecimal("2.00")).build());
		entityManager.persist(SellDetails.builder().sell(sell1).product(product).quantity(2).discount(0).price(new BigDecimal("4.00")).build());
		entityManager.persist(SellDetails.builder().sell(sell2).product(product).quantity(3).discount(0).price(new BigDecimal("6.00")).build());
		entityManager.flush();

		List<SellDetails> details = sellDetailsRepo.findBySell(sell1);

		assertThat(details).hasSize(2);
		assertThat(details).allMatch(d -> d.getSell().getId().equals(sell1.getId()));
	}
}
