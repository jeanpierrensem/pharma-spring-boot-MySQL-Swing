package com.officine.losto.s1.organisation.bootstrap;

import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.dto.MagasinCentralRequestDto;
import com.officine.losto.s1.organisation.dto.PointDeVenteRequestDto;
import com.officine.losto.s1.organisation.dto.SiteRequestDto;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import com.officine.losto.s1.organisation.service.MagasinCentralService;
import com.officine.losto.s1.organisation.service.PointDeVenteService;
import com.officine.losto.s1.organisation.service.SiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Jeu de données de démonstration pour Sites, magasins centraux et points de vente.
 * S’exécute uniquement en profil {@code dev}, et seulement si la table des sites est vide
 * (ne modifie pas une base déjà peuplée).
 */
@Component
@Profile("dev")
@Order(200)
@RequiredArgsConstructor
@Slf4j
public class OrganisationDemoDataSeeder implements ApplicationRunner {

	private final SiteRepository siteRepository;
	private final SiteService siteService;
	private final MagasinCentralService magasinCentralService;
	private final PointDeVenteService pointDeVenteService;

	@Override
	public void run(ApplicationArguments args) {
		if (siteRepository.count() > 0) {
			return;
		}
		log.info("Organisation : insertion des données de démo (sites, magasins centraux, PDV)…");

		Site paris = siteService.create(SiteRequestDto.builder()
				.code("DEMO-PARIS")
				.libelle("Pharmacie démo — Paris Centre")
				.actif(true)
				.build());
		Site lyon = siteService.create(SiteRequestDto.builder()
				.code("DEMO-LYON")
				.libelle("Pharmacie démo — Lyon Part-Dieu")
				.actif(true)
				.build());

		magasinCentralService.create(MagasinCentralRequestDto.builder()
				.siteId(paris.getId())
				.code("DEMO-MC-PARIS")
				.libelle("Magasin central Paris")
				.build());
		magasinCentralService.create(MagasinCentralRequestDto.builder()
				.siteId(lyon.getId())
				.code("DEMO-MC-LYON")
				.libelle("Magasin central Lyon")
				.build());

		pointDeVenteService.create(PointDeVenteRequestDto.builder()
				.siteId(paris.getId())
				.code("DEMO-PDV-PARIS-CAISSE")
				.libelle("Caisse principale")
				.adresse("12 rue de la Paix, 75002 Paris")
				.phone("0142000001")
				.actif(true)
				.build());
		pointDeVenteService.create(PointDeVenteRequestDto.builder()
				.siteId(paris.getId())
				.code("DEMO-PDV-PARIS-ORTHO")
				.libelle("Espace orthopédie")
				.adresse("12 rue de la Paix, 75002 Paris")
				.phone("0142000002")
				.actif(true)
				.build());
		pointDeVenteService.create(PointDeVenteRequestDto.builder()
				.siteId(lyon.getId())
				.code("DEMO-PDV-LYON-CAISSE")
				.libelle("Caisse hall A")
				.adresse("17 bd Vivier Merle, 69003 Lyon")
				.phone("0478000001")
				.actif(true)
				.build());

		log.info("Organisation : données de démo créées (2 sites, 2 magasins centraux, 3 PDV).");
	}
}
