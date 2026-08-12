package com.officine.losto.s1.organisation;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class OrganisationRepositoryTest {

	@Autowired
	private SiteRepository siteRepository;

	@Autowired
	private MagasinCentralRepository magasinCentralRepository;

	@Autowired
	private PointDeVenteRepository pointDeVenteRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void magasinCentral_findBySiteId_returnsOptionalWithEntity() {
		Site site = persistSite("S-REPO-1", "Site test");
		MagasinCentral mc = MagasinCentral.builder()
				.site(site)
				.code("MC-1")
				.libelle("Magasin central 1")
				.build();
		entityManager.persist(mc);
		entityManager.flush();
		entityManager.clear();

		assertThat(magasinCentralRepository.findBySite_Id(site.getId()))
				.isPresent()
				.get()
				.satisfies(m -> {
					assertThat(m.getCode()).isEqualTo("MC-1");
					assertThat(m.getSite().getId()).isEqualTo(site.getId());
				});
	}

	@Test
	void magasinCentral_secondRowForSameSite_violatesUniqueConstraint() {
		Site site = persistSite("S-REPO-2", "Site doublon");
		entityManager.persist(MagasinCentral.builder()
				.site(site)
				.code("MC-A")
				.libelle("Mag A")
				.build());
		entityManager.flush();

		assertThatThrownBy(() -> {
			entityManager.persist(MagasinCentral.builder()
					.site(site)
					.code("MC-B")
					.libelle("Mag B")
					.build());
			entityManager.flush();
		}).isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	void pointDeVente_findBySiteId_returnsAllForSite() {
		Site site = persistSite("S-REPO-3", "Site PDV");
		entityManager.persist(PointDeVente.builder()
				.site(site)
				.code("PDV-1")
				.libelle("Caisse 1")
				.actif(true)
				.build());
		entityManager.persist(PointDeVente.builder()
				.site(site)
				.code("PDV-2")
				.libelle("Caisse 2")
				.actif(true)
				.build());
		entityManager.flush();
		entityManager.clear();

		assertThat(pointDeVenteRepository.findBySite_Id(site.getId()))
				.hasSize(2)
				.extracting(PointDeVente::getCode)
				.containsExactlyInAnyOrder("PDV-1", "PDV-2");
	}

	@Test
	void site_existsByCode() {
		persistSite("S-UNIQUE", "Libellé");
		entityManager.flush();

		assertThat(siteRepository.existsByCode("S-UNIQUE")).isTrue();
		assertThat(siteRepository.existsByCode("INCONNU")).isFalse();
	}

	private Site persistSite(String code, String libelle) {
		Site site = Site.builder()
				.code(code)
				.libelle(libelle)
				.actif(true)
				.build();
		entityManager.persist(site);
		entityManager.flush();
		return site;
	}
}
