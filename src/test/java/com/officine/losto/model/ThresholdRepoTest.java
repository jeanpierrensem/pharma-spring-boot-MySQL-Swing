package com.officine.losto.model;

import com.officine.losto.entity.Threshold;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ThresholdRepoTest {

	@Autowired
	private ThresholdRepo thresholdRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_andSearch_returnExpectedRows() {
		entityManager.persist(Threshold.builder().code("TH-LOW").description("Low level").level(5).build());
		entityManager.persist(Threshold.builder().code("TH-HIGH").description("High level").level(20).build());
		entityManager.flush();

		Threshold found = thresholdRepo.findByCode("TH-LOW");
		List<Threshold> searched = thresholdRepo.findByCodeContainingOrDescriptionContaining("HIGH", "Low");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
