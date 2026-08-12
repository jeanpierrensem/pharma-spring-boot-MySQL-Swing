package com.officine.losto.model;

import com.officine.losto.entity.Batch;
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
class BatchRepoTest {

	@Autowired
	private BatchRepo batchRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByNumber_andFindByNumberContaining_returnExpectedBatches() {
		entityManager.persist(Batch.builder().number("LOT-001").expiredDate(LocalDate.of(2030, 1, 1)).quantity(10).build());
		entityManager.persist(Batch.builder().number("LOT-ABC").expiredDate(LocalDate.of(2030, 1, 1)).quantity(5).build());
		entityManager.flush();

		Batch exact = batchRepo.findByNumber("LOT-001");
		List<Batch> partial = batchRepo.findByNumberContaining("LOT");

		assertThat(exact).isNotNull();
		assertThat(exact.getNumber()).isEqualTo("LOT-001");
		assertThat(partial).hasSize(2);
	}
}
