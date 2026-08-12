package com.officine.losto.model;

import com.officine.losto.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepoTest {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_returnsMatchingCategory() {
		Category saved = entityManager.persistAndFlush(Category.builder()
				.code("CAT-A")
				.description("Pain relief")
				.build());

		Category found = categoryRepo.findByCode("CAT-A");

		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(saved.getId());
		assertThat(found.getDescription()).isEqualTo("Pain relief");
	}

	@Test
	void findByDescriptionContainingOrCodeContaining_returnsFilteredResults() {
		entityManager.persist(Category.builder().code("CAT-PAIN").description("Pain products").build());
		entityManager.persist(Category.builder().code("CAT-COLD").description("Cold products").build());
		entityManager.persist(Category.builder().code("CAT-MISC").description("Miscellaneous").build());
		entityManager.flush();

		List<Category> result = categoryRepo
				.findByDescriptionContainingOrCodeContaining("Pain", "COLD");

		assertThat(result).hasSize(2);
		assertThat(result).extracting(Category::getCode)
				.containsExactlyInAnyOrder("CAT-PAIN", "CAT-COLD");
	}
}
