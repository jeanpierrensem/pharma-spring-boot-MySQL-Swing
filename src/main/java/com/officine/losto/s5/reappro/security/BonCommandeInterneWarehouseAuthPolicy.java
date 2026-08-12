package com.officine.losto.s5.reappro.security;

import com.officine.losto.entity.StatutBonCommandeInterne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Droits métier pour les décisions réservées au magasin central (bons internes envoyés ou annulation).
 *
 * <p>Activez avec {@code officine.bon.restrict-central-warehouse-role=true}, puis attribuez
 * {@code ROLE_MAGASIN_CENTRAL} aux comptes autorisés.
 */
@Slf4j
@Component
public class BonCommandeInterneWarehouseAuthPolicy {

	/** Rôle Spring Security pour les utilisateurs pouvant passer un bon ENVOYÉ → TRAITE ou ENVOYE → ANNULE. */
	public static final String ROLE_MAGASIN_CENTRAL = "ROLE_MAGASIN_CENTRAL";

	@Value("${officine.bon.restrict-central-warehouse-role:false}")
	private boolean restrictRole;

	public void assertWarehouseDecisionAllowed(
			StatutBonCommandeInterne ancienStatut, StatutBonCommandeInterne nouveauStatut) {
		if (!restrictRole) {
			return;
		}
		if (ancienStatut != StatutBonCommandeInterne.ENVOYE
				&& ancienStatut != StatutBonCommandeInterne.PARTIEL) {
			return;
		}
		if (nouveauStatut != StatutBonCommandeInterne.TRAITE
				&& nouveauStatut != StatutBonCommandeInterne.PARTIEL
				&& nouveauStatut != StatutBonCommandeInterne.ANNULE) {
			return;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new AccessDeniedException("Connexion obligatoire pour traiter ou annuler un bon envoyé au magasin central.");
		}
		boolean ok = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(ROLE_MAGASIN_CENTRAL::equals);
		if (!ok) {
			throw new AccessDeniedException(
					"Seuls les comptes ayant « magasin central » peuvent décider "
							+ ancienStatut
							+ " → "
							+ nouveauStatut
							+ ".");
		}
		if (log.isDebugEnabled()) {
			log.debug("Bon interne décision warehouse autorisée pour utilisateur {}", auth.getName());
		}
	}
}
