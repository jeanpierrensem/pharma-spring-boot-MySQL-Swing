package com.officine.losto.model;

import com.officine.losto.entity.DrugType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DrugTypeRepoTest {

	@Autowired
	private DrugTypeRepo drugTypeRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_andSearch_returnExpectedRows() {
		entityManager.persist(DrugType.builder().code("DT-A").description("Antibiotic").build());
		entityManager.persist(DrugType.builder().code("DT-B").description("Antiviral").build());
		entityManager.flush();

		DrugType found = drugTypeRepo.findByCode("DT-A");
		List<DrugType> searched = drugTypeRepo.findByDescriptionContainingOrCodeContaining("Antiv", "DT-A");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
