package com.officine.losto.model;

import com.officine.losto.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepoTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByNameAndPassword_returnsUserWhenCredentialsMatch() {
		AppUser user = AppUser.builder()
				.name("Alice")
				.login("alice-login")
				.email("alice@mail.com")
				.password("pwd123")
				.phoneNumber("0600000001")
				.build();
		entityManager.persistAndFlush(user);

		AppUser found = userRepo.findByNameAndPassword("Alice", "pwd123");

		assertThat(found).isNotNull();
		assertThat(found.getLogin()).isEqualTo("alice-login");
	}

	@Test
	void findByNameContainingOrLoginContainingOrEmailContaining_returnsMatchingUsers() {
		entityManager.persist(AppUser.builder().name("John Doe").login("jdoe").email("john@acme.com").password("x").build());
		entityManager.persist(AppUser.builder().name("Jane Roe").login("jroe").email("jane@acme.com").password("x").build());
		entityManager.persist(AppUser.builder().name("No Match").login("nmatch").email("none@nowhere.com").password("x").build());
		entityManager.flush();

		List<AppUser> users = userRepo.findByNameContainingOrLoginContainingOrEmailContaining("John", "jroe", "@acme.com");

		assertThat(users).extracting(AppUser::getName)
				.contains("John Doe", "Jane Roe");
	}
}
