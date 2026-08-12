package com.officine.losto.s7.stocks.domain;

/**
 * Type de mouvement de stock (persisté en colonne {@code TYPE_MOUVEMENT}).
 */
public enum TypeMouvementStock {
	ENTREE,
	SORTIE,
	AJUSTEMENT_INVENTAIRE,
	CORRECTION,
	TRANSFERT,
	RESERVATION,
	ANNULATION_RESERVATION,
	AUTRE
}
