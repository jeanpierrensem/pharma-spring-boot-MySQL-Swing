package com.officine.losto.model;

import com.officine.losto.entity.Sell;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SellRepoTest {

	@Autowired
	private SellRepo sellRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByNumber_returnsExpectedSell() {
		entityManager.persist(Sell.builder()
				.number("SELL-001")
				.dateVente(LocalDate.of(2026, 3, 10))
				.seller("John")
				.client("Jane")
				.sellType("COMPTANT")
				.paymentMode("Cash")
				.totalPrice(new BigDecimal("20.00"))
				.amountReceived(new BigDecimal("20.00"))
				.changeGiven(BigDecimal.ZERO)
				.remark("ok")
				.build());
		entityManager.flush();

		Sell found = sellRepo.findByNumber("SELL-001");

		assertThat(found).isNotNull();
		assertThat(found.getSeller()).isEqualTo("John");
	}
}
