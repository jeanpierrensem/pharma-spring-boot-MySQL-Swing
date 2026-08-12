package com.officine.losto.model;

import com.officine.losto.entity.Orders;
import com.officine.losto.entity.OrdersDetails;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Statut;
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
class OrderDetailsRepoTest {

	@Autowired
	private OrderDetailsRepo orderDetailsRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByOrders_andDeleteByOrders_workAsExpected() {
		Orders order = entityManager.persist(Orders.builder()
				.number("ORD-10")
				.orderDate(LocalDate.of(2026, 2, 10))
				.description("Main order")
				.statut(Statut.NON)
				.build());
		Product product = entityManager.persist(Product.builder()
				.name("Aspirin")
				.codeBar("ASP-001")
				.build());
		OrdersDetails details = entityManager.persist(OrdersDetails.builder()
				.orders(order)
				.product(product)
				.quantity(2)
				.unitPrice(3)
				.discount(0)
				.totalPrice(6)
				.build());
		entityManager.flush();

		OrdersDetails found = orderDetailsRepo.findByOrders(order);
		long deleted = orderDetailsRepo.deleteByOrders(order);
		entityManager.flush();

		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(details.getId());
		assertThat(deleted).isEqualTo(1);
		assertThat(orderDetailsRepo.findByOrders(order)).isNull();
	}
}
