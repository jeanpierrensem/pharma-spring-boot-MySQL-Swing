package com.officine.losto.model;

import com.officine.losto.entity.Orders;
import com.officine.losto.entity.Statut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrdersRepoTest {

	@Autowired
	private OrdersRepo ordersRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByNumber_andSearch_returnExpectedOrders() {
		entityManager.persist(Orders.builder().number("CMD-001").orderDate(LocalDate.of(2026, 1, 1)).description("First order").statut(Statut.NON).build());
		entityManager.persist(Orders.builder().number("CMD-002").orderDate(LocalDate.of(2026, 1, 2)).description("Second order").statut(Statut.COMPLETE).build());
		entityManager.flush();

		Orders found = ordersRepo.findByNumber("CMD-001");
		List<Orders> searched = ordersRepo.findByNumberContainingOrOrderDateContainingOrDescriptionContaining("CMD", "01-02", "First");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
