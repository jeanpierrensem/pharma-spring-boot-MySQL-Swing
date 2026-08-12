package com.officine.losto.model;

import com.officine.losto.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepoTest {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByName_andFindByCodeBar_returnMatchingProduct() {
		Product product = Product.builder()
				.name("Paracetamol 500")
				.codeBar("000111222333")
				.build();
		entityManager.persistAndFlush(product);

		Product byName = productRepo.findByName("Paracetamol 500");
		Product byCodeBar = productRepo.findByCodeBar("000111222333");

		assertThat(byName).isNotNull();
		assertThat(byCodeBar).isNotNull();
		assertThat(byName.getId()).isEqualTo(byCodeBar.getId());
	}

	@Test
	void findByNameContainingOrCodeBarContaining_returnsMatchingProducts() {
		entityManager.persist(Product.builder().name("Paracetamol").codeBar("C001").build());
		entityManager.persist(Product.builder().name("Ibuprofen").codeBar("C002-IBU").build());
		entityManager.persist(Product.builder().name("Vitamins").codeBar("C003").build());
		entityManager.flush();

		List<Product> products = productRepo.findByNameContainingOrCodeBarContaining("Para", "IBU");

		assertThat(products).hasSize(2);
		assertThat(products).extracting(Product::getName)
				.containsExactlyInAnyOrder("Paracetamol", "Ibuprofen");
	}
}
