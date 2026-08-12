package com.officine.losto.security.menu;

import com.officine.losto.catalog.MenuSecurityCatalog;
import java.util.List;
import org.springframework.http.HttpMethod;

/**
 * Correspondance endpoints REST ↔ pathCode (ordre : règles les plus spécifiques en premier).
 */
public final class ApiMenuPermissionRegistry {

	private static final List<ApiMenuPermissionRule> RULES = List.of(
			// --- Sécurité ---
			r(HttpMethod.POST, "/api/groups/*/menus", "nav.access.main.default", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/groups/**", "nav.access.main", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/menus/**", "nav.access.main", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.POST, "/api/users/export/pdf", "nav.access.users.export", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.POST, "/api/users/print/pdf", "nav.access.users.print", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/users/*", "nav.access.users.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/users/**", "nav.access.users", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Référentiel ---
			r(HttpMethod.POST, "/api/providers", "nav.referential.providers.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/providers", "nav.referential.providers.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/providers/*", "nav.referential.providers.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/providers/**", "nav.referential.providers", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.POST, "/api/batches", "nav.referential.batches.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/batches", "nav.referential.batches.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/batches/*", "nav.referential.batches.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/batches/**", "nav.referential.batches", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/categories/**", "nav.referential.masterdata.categories", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/drug-types/**", "nav.referential.masterdata.types", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/sections/**", "nav.referential.masterdata.sections", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/thresholds/**", "nav.referential.masterdata.thresholds", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/forms/**", "nav.referential.masterdata.forms", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/packagings/**", "nav.referential.masterdata.packaging", ApiMenuPermissionRule.MatchMode.EXACT),

			// --- Organisation ---
			r(HttpMethod.POST, "/api/sites", "nav.organisation.sites.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/sites", "nav.organisation.sites.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/sites/*", "nav.organisation.sites.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/sites/**", "nav.organisation.sites", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.POST, "/api/magasins-centraux", "nav.organisation.central_stores.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/magasins-centraux", "nav.organisation.central_stores.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/magasins-centraux/*", "nav.organisation.central_stores.delete",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/magasins-centraux/**", "nav.organisation.central_stores", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.POST, "/api/points-de-vente", "nav.organisation.sales_outlets.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/points-de-vente", "nav.organisation.sales_outlets.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/points-de-vente/*", "nav.organisation.sales_outlets.delete",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/points-de-vente/**", "nav.organisation.sales_outlets", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Approvisionnement ---
			r(HttpMethod.POST, "/api/products", "nav.approvisionnement.products.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/products", "nav.approvisionnement.products.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.POST, "/api/products/export/pdf", "nav.approvisionnement.products.manage",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.POST, "/api/products/print/pdf", "nav.approvisionnement.products.manage",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/products/*", "nav.approvisionnement.products.delete",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/products/**", "nav.approvisionnement.products", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.DELETE, "/api/orders/*", "nav.approvisionnement.orders.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/orders/**", "nav.approvisionnement.orders", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/order-details/**", "nav.approvisionnement.orders", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.DELETE, "/api/receipt-details/*", "nav.approvisionnement.receipts.delete",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/receipt-details/**", "nav.approvisionnement.receipts", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Ventes ---
			r(HttpMethod.POST, "/api/sells", "nav.sales.ticket.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/sells", "nav.sales.cashdesk.collect", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/sells/*", "nav.sales.ticket.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/sells/**", "nav.sales.history", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/sell-details/**", "nav.sales.ticket", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/affectations-vendeur/**", "nav.sales.planning", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Stock ---
			r(HttpMethod.POST, "/api/stock-central/adjust-disponible", "nav.stock.central.adjust",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/stock-central/*", "nav.stock.central.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.POST, "/api/stock-central", "nav.stock.central.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/stock-central", "nav.stock.central.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/stock-central/**", "nav.stock.central", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.POST, "/api/stock-pdv/adjust-disponible", "nav.stock.pdv.adjust", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.DELETE, "/api/stock-pdv/*", "nav.stock.pdv.delete", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.POST, "/api/stock-pdv", "nav.stock.pdv.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/stock-pdv", "nav.stock.pdv.manage", ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/stock-pdv/**", "nav.stock.pdv", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/mouvements-stock/**", "nav.stock.movements", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Bons internes ---
			r(HttpMethod.POST, "/api/bons-commande-interne/traiter", "nav.stock.central.internal_receipts.receive",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.GET, "/api/bons-commande-interne/en-cours-traitement", "nav.stock.central.internal_receipts",
					ApiMenuPermissionRule.MatchMode.SCREEN),
			r(HttpMethod.DELETE, "/api/bons-commande-interne/*", "nav.stock.pdv.internal_orders.delete",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.POST, "/api/bons-commande-interne", "nav.stock.pdv.internal_orders.draft",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(HttpMethod.PUT, "/api/bons-commande-interne", "nav.stock.pdv.internal_orders.manage",
					ApiMenuPermissionRule.MatchMode.EXACT),
			r(null, "/api/bons-commande-interne/**", "nav.stock.pdv.internal_orders", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Pilotage / dashboard ---
			r(null, "/api/dashboard/**", "nav.pilotage.dashboard", ApiMenuPermissionRule.MatchMode.SCREEN),
			r(null, "/api/pilotage/**", "nav.pilotage.dashboard", ApiMenuPermissionRule.MatchMode.SCREEN),

			// --- Fallback lecture API authentifiée (référentiels croisés) ---
			r(HttpMethod.GET, "/api/**", MenuSecurityCatalog.NavRoots.REFERENTIAL, ApiMenuPermissionRule.MatchMode.SCREEN)
	);

	private ApiMenuPermissionRegistry() {
	}

	public static List<ApiMenuPermissionRule> rules() {
		return RULES;
	}

	private static ApiMenuPermissionRule r(HttpMethod method, String pattern, String pathCode,
			ApiMenuPermissionRule.MatchMode mode) {
		return new ApiMenuPermissionRule(method, pattern, pathCode, mode);
	}
}
