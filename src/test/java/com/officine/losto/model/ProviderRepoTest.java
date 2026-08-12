package com.officine.losto.model;

import com.officine.losto.entity.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProviderRepoTest {

	@Autowired
	private ProviderRepo providerRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByCode_andSearch_returnExpectedProviders() {
		entityManager.persist(Provider.builder().code("PR-01").designation("Supplier 1").phoneNumber("0600000001").build());
		entityManager.persist(Provider.builder().code("PR-02").designation("Supplier 2").phoneNumber("0700000002").build());
		entityManager.flush();

		Provider found = providerRepo.findByCode("PR-01");
		List<Provider> searched = providerRepo.findByPhoneNumberContainingOrCodeContaining("0700", "PR-01");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
