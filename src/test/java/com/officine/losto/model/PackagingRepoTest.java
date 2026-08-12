package com.officine.losto.model;

import com.officine.losto.entity.Packaging;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PackagingRepoTest {

	@Autowired
	private PackagingRepo packagingRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_andSearch_returnExpectedRows() {
		entityManager.persist(Packaging.builder().code("PK-BX").description("Box").build());
		entityManager.persist(Packaging.builder().code("PK-BTL").description("Bottle").build());
		entityManager.flush();

		Packaging found = packagingRepo.findByCode("PK-BX");
		List<Packaging> searched = packagingRepo.findByDescriptionContainingOrCodeContaining("Bot", "PK-BX");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
