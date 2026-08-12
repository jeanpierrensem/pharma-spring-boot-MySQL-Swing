package com.officine.losto.entity;

/**
 * Cycle de vie d'un bon de commande interne (réappro caisse → magasin central).
 *
 * <p>Transitions autorisées : {@link #validateTransitionTo(StatutBonCommandeInterne)}.
 */
public enum StatutBonCommandeInterne {

	/** Brouillon éditable côté point de vente. */
	BROUILLON("Brouillon"),
	/** Envoyé vers le magasin central, en attente de traitement. */
	ENVOYE("Envoyé"),
	/** Livraison partielle : au moins une ligne avec quantité livrée cumulée &lt; quantité commandée. */
	PARTIEL("Partiel"),
	/** Traitée par le magasin central (réapprovisionnement effectué / clos). */
	TRAITE("Traité"),
	/** Annulée (ne sera pas traitée). */
	ANNULE("Annulé");

	private final String libelle;

	StatutBonCommandeInterne(String libelle) {
		this.libelle = libelle;
	}

	public String getLibelle() {
		return libelle;
	}

	/**
	 * Vérifie une transition depuis ce statut vers {@code target}.
	 *
	 * @throws IllegalArgumentException si la transition est interdite
	 */
	public void validateTransitionTo(StatutBonCommandeInterne target) {
		if (target == null) {
			throw new IllegalArgumentException("Statut cible obligatoire");
		}
		if (this == target) {
			return;
		}
		switch (this) {
			case BROUILLON -> {
				if (target != ENVOYE && target != ANNULE) {
					throw new IllegalArgumentException(
							"Depuis BROUILLON : seuls ENVOYE et ANNULE sont autorisés");
				}
			}
			case ENVOYE -> {
				if (target != TRAITE && target != PARTIEL && target != ANNULE) {
					throw new IllegalArgumentException(
							"Depuis ENVOYE : seuls TRAITE, PARTIEL et ANNULE sont autorisés");
				}
			}
			case PARTIEL -> {
				if (target != TRAITE && target != ANNULE) {
					throw new IllegalArgumentException(
							"Depuis PARTIEL : seuls TRAITE et ANNULE sont autorisés");
				}
			}
			case TRAITE, ANNULE -> throw new IllegalArgumentException(
					"Bon en statut terminal (" + this + ") : changement de statut interdit");
		}
	}
}
