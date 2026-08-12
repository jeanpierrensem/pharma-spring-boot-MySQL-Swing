package com.officine.losto.model;

import com.officine.losto.entity.AppGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class GroupRepoTest {

	@Autowired
	private GroupRepo groupRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findGroupByName_andSearch_returnExpectedGroups() {
		entityManager.persist(AppGroup.builder().name("Administrators").description("System admins").selected(true).build());
		entityManager.persist(AppGroup.builder().name("Consultants").description("Medical consultants").selected(false).build());
		entityManager.flush();

		AppGroup found = groupRepo.findGroupByName("Administrators");
		List<AppGroup> searched = groupRepo.findByDescriptionContainingOrNameContaining("Medical", "Admin");

		assertThat(found).isNotNull();
		assertThat(found.getName()).isEqualTo("Administrators");
		assertThat(searched).hasSize(2);
	}
}
