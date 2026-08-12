package com.officine.losto.catalog;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class MenuImplicitPermissionCatalogTest {

	@Test
	void salesTicket_grantsOrganisationAndProductsRead() {
		Set<String> explicit = Set.of("nav.sales.ticket", "nav.sales.ticket.manage");
		assertTrue(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.organisation.sites"));
		assertTrue(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.organisation.sales_outlets"));
		assertTrue(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.approvisionnement.products"));
		assertTrue(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.access.users"));
	}

	@Test
	void pilotageOnly_grantsSitesReadNotProducts() {
		Set<String> explicit = Set.of("nav.pilotage.dashboard.read");
		assertTrue(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.organisation.sites"));
		assertFalse(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.approvisionnement.products"));
	}

	@Test
	void effectiveApiPathCodes_mergesExplicitAndImplicit() {
		Set<String> explicit = Set.of("nav.sales.ticket");
		Set<String> effective = MenuImplicitPermissionCatalog.effectiveApiPathCodes(explicit);
		assertTrue(effective.contains("nav.sales.ticket"));
		assertTrue(effective.contains("nav.organisation.sites"));
	}

	@Test
	void noTrigger_noImplicitGrant() {
		Set<String> explicit = Set.of("nav.access.main");
		assertFalse(MenuImplicitPermissionCatalog.isImplicitReadGranted(explicit, "nav.organisation.sites"));
	}
}
