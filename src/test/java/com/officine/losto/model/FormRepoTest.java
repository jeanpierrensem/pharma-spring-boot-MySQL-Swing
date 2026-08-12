package com.officine.losto.model;

import com.officine.losto.entity.Form;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FormRepoTest {

	@Autowired
	private FormRepo formRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_andSearch_returnExpectedRows() {
		entityManager.persist(Form.builder().code("F-CAP").description("Capsule").build());
		entityManager.persist(Form.builder().code("F-SYR").description("Syrup").build());
		entityManager.flush();

		Form found = formRepo.findByCode("F-CAP");
		List<Form> searched = formRepo.findByDescriptionContainingOrCodeContaining("Syr", "F-CAP");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
