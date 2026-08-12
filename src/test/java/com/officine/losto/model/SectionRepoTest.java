package com.officine.losto.model;

import com.officine.losto.entity.Section;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SectionRepoTest {

	@Autowired
	private SectionRepo sectionRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_andSearch_returnExpectedRows() {
		entityManager.persist(Section.builder().code("SEC-A").description("A section").build());
		entityManager.persist(Section.builder().code("SEC-B").description("B section").build());
		entityManager.flush();

		Section found = sectionRepo.findByCode("SEC-A");
		List<Section> searched = sectionRepo.findByDescriptionContainingOrCodeContaining("B sec", "SEC-A");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
