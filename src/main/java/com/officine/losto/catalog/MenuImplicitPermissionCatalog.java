package com.officine.losto.catalog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Droits de lecture implicites pour les appels API (GET) : lorsqu'un groupe possède un écran
 * métier (ex. {@code nav.sales.ticket}), les référentiels nécessaires aux combos et filtres
 * (sites, PDV, produits…) sont accordés automatiquement sans cocher manuellement chaque
 * entrée dans Sécurité → Habilitations. S'applique à tout groupe existant ou futur.
 */
public final class MenuImplicitPermissionCatalog {

	private record ReadGrant(String grantPathCode, String... grantedIfAnyPathUnder) {
	}

	private static final List<ReadGrant> READ_GRANTS = List.of(
			// --- Organisation (combos site / PDV / magasin central) ---
			grant("nav.organisation.sites",
					MenuSecurityCatalog.NavRoots.SALES,
					MenuSecurityCatalog.NavRoots.PILOTAGE,
					MenuSecurityCatalog.NavRoots.STOCK,
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT),
			grant("nav.organisation.sales_outlets",
					MenuSecurityCatalog.NavRoots.SALES,
					MenuSecurityCatalog.NavRoots.STOCK,
					MenuSecurityCatalog.NavRoots.PILOTAGE),
			grant("nav.organisation.central_stores",
					MenuSecurityCatalog.NavRoots.STOCK,
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT),

			// --- Catalogue produits ---
			grant("nav.approvisionnement.products",
					MenuSecurityCatalog.NavRoots.SALES,
					MenuSecurityCatalog.NavRoots.STOCK,
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT),

			// --- Utilisateurs (combo vendeur, planning, bons internes) ---
			grant("nav.access.users",
					MenuSecurityCatalog.NavRoots.SALES,
					MenuSecurityCatalog.NavRoots.STOCK),

			// --- Référentiel (formulaires produits, commandes, stock) ---
			grant("nav.referential.providers", MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT),
			grant("nav.referential.batches",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.STOCK),
			grant("nav.referential.masterdata.categories",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.REFERENTIAL),
			grant("nav.referential.masterdata.types",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.REFERENTIAL),
			grant("nav.referential.masterdata.sections",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.REFERENTIAL),
			grant("nav.referential.masterdata.thresholds",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.STOCK),
			grant("nav.referential.masterdata.forms",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.REFERENTIAL),
			grant("nav.referential.masterdata.packaging",
					MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT,
					MenuSecurityCatalog.NavRoots.REFERENTIAL),

			// --- Commandes / réceptions (approvisionnement croisé) ---
			grant("nav.approvisionnement.orders", MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT),
			grant("nav.approvisionnement.receipts", MenuSecurityCatalog.NavRoots.APPROVISIONNEMENT),

			// --- Stock croisé (bons internes, mouvements) ---
			grant("nav.stock.central", MenuSecurityCatalog.NavRoots.STOCK),
			grant("nav.stock.pdv", MenuSecurityCatalog.NavRoots.STOCK),
			grant("nav.stock.movements", MenuSecurityCatalog.NavRoots.STOCK)
	);

	private MenuImplicitPermissionCatalog() {
	}

	/**
	 * pathCodes accordés implicitement en lecture API à partir des habilitations explicites du groupe.
	 */
	public static Set<String> implicitReadGrants(Set<String> explicitAllowed) {
		if (explicitAllowed == null || explicitAllowed.isEmpty()) {
			return Set.of();
		}
		if (explicitAllowed.contains("*")) {
			return Set.of();
		}
		Set<String> grants = new HashSet<>();
		for (ReadGrant rule : READ_GRANTS) {
			if (matchesAnyTrigger(explicitAllowed, rule.grantedIfAnyPathUnder())) {
				grants.add(rule.grantPathCode());
			}
		}
		return Set.copyOf(grants);
	}

	/**
	 * Union habilitations explicites + droits implicites (pour garde API et client JavaFX).
	 */
	public static Set<String> effectiveApiPathCodes(Set<String> explicitAllowed) {
		if (explicitAllowed == null || explicitAllowed.isEmpty()) {
			return Set.of();
		}
		if (explicitAllowed.contains("*")) {
			return Set.of("*");
		}
		Set<String> merged = new HashSet<>(explicitAllowed);
		merged.addAll(implicitReadGrants(explicitAllowed));
		return Set.copyOf(merged);
	}

	public static boolean isImplicitReadGranted(Set<String> explicitAllowed, String requiredScreenPathCode) {
		if (requiredScreenPathCode == null || requiredScreenPathCode.isBlank()) {
			return true;
		}
		if (explicitAllowed == null || explicitAllowed.isEmpty()) {
			return false;
		}
		for (ReadGrant rule : READ_GRANTS) {
			if (rule.grantPathCode().equals(requiredScreenPathCode)
					&& matchesAnyTrigger(explicitAllowed, rule.grantedIfAnyPathUnder())) {
				return true;
			}
		}
		return false;
	}

	private static ReadGrant grant(String grantPathCode, String... triggers) {
		return new ReadGrant(grantPathCode, triggers);
	}

	private static boolean matchesAnyTrigger(Set<String> explicitAllowed, String... triggerPrefixes) {
		for (String prefix : triggerPrefixes) {
			if (hasPathUnder(explicitAllowed, prefix)) {
				return true;
			}
		}
		return false;
	}

	static boolean hasPathUnder(Set<String> paths, String prefix) {
		if (prefix == null || prefix.isBlank()) {
			return false;
		}
		for (String path : paths) {
			if (path == null || path.isBlank()) {
				continue;
			}
			if (path.equals(prefix) || path.startsWith(prefix + ".")) {
				return true;
			}
		}
		return false;
	}
}
