package com.officine.losto.model;

import com.officine.losto.entity.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MenuRepoTest {

	@Autowired
	private MenuRepo menuRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByName_andSearch_returnExpectedMenus() {
		entityManager.persist(Menu.builder().name("Inventory").description("Stock module").active(true).build());
		entityManager.persist(Menu.builder().name("Sales").description("Cash operations").active(true).build());
		entityManager.flush();

		Menu found = menuRepo.findByName("Inventory");
		List<Menu> searched = menuRepo.findByDescriptionContainingOrNameContaining("Cash", "Inventory");

		assertThat(found).isNotNull();
		assertThat(searched).hasSize(2);
	}
}
