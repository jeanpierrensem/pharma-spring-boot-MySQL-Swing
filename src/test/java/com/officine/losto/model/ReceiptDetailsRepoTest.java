package com.officine.losto.model;

import com.officine.losto.entity.Orders;
import com.officine.losto.entity.OrdersDetails;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.ReceiptDetails;
import com.officine.losto.entity.Statut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReceiptDetailsRepoTest {

	@Autowired
	private ReceiptDetailsRepo receiptDetailsRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByOrdersDetails_andDeleteByOrdersDetails_workAsExpected() {
		Orders order = entityManager.persist(Orders.builder()
				.number("ORD-RCP")
				.orderDate(LocalDate.of(2026, 3, 1))
				.description("Receipt order")
				.statut(Statut.NON)
				.build());
		Product product = entityManager.persist(Product.builder()
				.name("Ibuprofen")
				.codeBar("IBU-001")
				.build());
		OrdersDetails details = entityManager.persist(OrdersDetails.builder()
				.orders(order)
				.product(product)
				.quantity(3)
				.unitPrice(6)
				.discount(0)
				.totalPrice(18)
				.build());
		entityManager.persist(ReceiptDetails.builder().ordersDetails(details).receivedQuantity(2).missingQuantity(1).date(LocalDateTime.of(2026, 3, 2, 10, 0)).observation("part 1").build());
		entityManager.persist(ReceiptDetails.builder().ordersDetails(details).receivedQuantity(1).missingQuantity(0).date(LocalDateTime.of(2026, 3, 2, 11, 0)).observation("part 2").build());
		entityManager.flush();

		List<ReceiptDetails> found = receiptDetailsRepo.findByOrdersDetails(details);
		long deleted = receiptDetailsRepo.deleteByOrdersDetails(details);
		entityManager.flush();

		assertThat(found).hasSize(2);
		assertThat(deleted).isEqualTo(2);
		assertThat(receiptDetailsRepo.findByOrdersDetails(details)).isEmpty();
	}
}
