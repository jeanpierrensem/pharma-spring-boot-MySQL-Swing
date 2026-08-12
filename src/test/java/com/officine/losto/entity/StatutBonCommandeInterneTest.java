package com.officine.losto.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class StatutBonCommandeInterneTest {

	@Test
	void libellesFrancais() {
		assertThat(StatutBonCommandeInterne.BROUILLON.getLibelle()).isEqualTo("Brouillon");
		assertThat(StatutBonCommandeInterne.ENVOYE.getLibelle()).isEqualTo("Envoyé");
		assertThat(StatutBonCommandeInterne.PARTIEL.getLibelle()).isEqualTo("Partiel");
		assertThat(StatutBonCommandeInterne.TRAITE.getLibelle()).isEqualTo("Traité");
		assertThat(StatutBonCommandeInterne.ANNULE.getLibelle()).isEqualTo("Annulé");
	}

	@Test
	void transitionsAutorisees() {
		assertThatCode(() -> StatutBonCommandeInterne.BROUILLON.validateTransitionTo(StatutBonCommandeInterne.ENVOYE))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.BROUILLON.validateTransitionTo(StatutBonCommandeInterne.ANNULE))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.ENVOYE.validateTransitionTo(StatutBonCommandeInterne.PARTIEL))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.PARTIEL.validateTransitionTo(StatutBonCommandeInterne.TRAITE))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.ENVOYE.validateTransitionTo(StatutBonCommandeInterne.TRAITE))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.ENVOYE.validateTransitionTo(StatutBonCommandeInterne.ANNULE))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.BROUILLON.validateTransitionTo(StatutBonCommandeInterne.BROUILLON))
				.doesNotThrowAnyException();
		assertThatCode(() -> StatutBonCommandeInterne.ENVOYE.validateTransitionTo(StatutBonCommandeInterne.ENVOYE))
				.doesNotThrowAnyException();
	}

	@Test
	void transitionsInterdites() {
		assertThatThrownBy(() -> StatutBonCommandeInterne.BROUILLON.validateTransitionTo(StatutBonCommandeInterne.TRAITE))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> StatutBonCommandeInterne.TRAITE.validateTransitionTo(StatutBonCommandeInterne.ANNULE))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> StatutBonCommandeInterne.ANNULE.validateTransitionTo(StatutBonCommandeInterne.BROUILLON))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> StatutBonCommandeInterne.BROUILLON.validateTransitionTo(null))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
