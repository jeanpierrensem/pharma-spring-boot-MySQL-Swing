package com.officine.losto.catalog;

import java.util.List;

/**
 * Catalogue d’entrées de menu (pathCode stables) pour la synchro base et le seed des groupes.
 */
public final class MenuSecurityCatalog {

	private MenuSecurityCatalog() {
	}

	/** Racine unique du catalogue ({@link #fullCatalog()}) pour les seeds de groupes ({@code menusForModuleSubtrees}). */
	public static final class NavRoots {
		/**
		 * Racine « Sécurité » (habilitations + utilisateurs). Anciennement {@code nav.dashboard} pour les seeds de groupes.
		 */
		public static final String DASHBOARD = "nav.access";
		/** Racine « Référentiel » (fournisseurs, lots, données de base). */
		public static final String REFERENTIAL = "nav.referential";
		/** Racine « Organisation » (sites, magasins centraux, points de vente). */
		public static final String ORGANISATION = "nav.organisation";
		/** Racine « Approvisionnement » (produits, commandes, réception). */
		public static final String APPROVISIONNEMENT = "nav.approvisionnement";
		/** Racine « Ventes » (saisie ticket, caisse, historique, affectation). */
		public static final String SALES = "nav.sales";
		/** Racine « Stock » (central, PDV, mouvements, bons internes). */
		public static final String STOCK = "nav.stock";
		/** Racine « Pilotage » (tableau de bord synthèse). */
		public static final String PILOTAGE = "nav.pilotage";

		private NavRoots() {
		}
	}

	public record Node(String pathCode, String name, String description, int treeLevel, int sortOrder, List<Node> children) {
		public Node(String pathCode, String name, String description, int treeLevel, int sortOrder) {
			this(pathCode, name, description, treeLevel, sortOrder, List.of());
		}
	}

	/**
	 * Arbre habilitation : Sécurité, Référentiel, Organisation, Approvisionnement, Ventes, Stock, Pilotage.
	 */
	public static List<Node> fullCatalog() {
		return List.of(
				accessRoot(),
				referentialRoot(),
				organisationRoot(),
				approvisionnementRoot(),
				salesRoot(),
				stockRoot(),
				pilotageRoot());
	}

	private static Node accessRoot() {
		return new Node("nav.access", "Sécurité", "Habilitations, groupes et comptes utilisateurs", 0, 0, List.of(
				new Node("nav.access.main", "Habilitation", "Gestion des droits menu par groupe", 1, 0, List.of(
						new Node("nav.access.main.default", "Ajouter/Modifier",
								"Création et mise à jour des habilitations", 3, 0),
						new Node("nav.access.main.delete", "Supprimer",
								"Retrait de droits menu", 3, 1))),
				new Node("nav.access.users", "Utilisateur", "Comptes applicatifs", 1, 1, List.of(
						new Node("nav.access.users.manage", "Ajouter/Modifier",
								"Création et modification de comptes", 2, 0),
						new Node("nav.access.users.delete", "Supprimer",
								"Suppression ou désactivation de comptes", 2, 1),
						new Node("nav.access.users.print", "Imprimer",
								"Impression de fiches ou listes utilisateurs", 2, 2),
						new Node("nav.access.users.export", "Exporter",
								"Export des données utilisateurs", 2, 3)))));
	}

	private static Node referentialRoot() {
		return new Node(NavRoots.REFERENTIAL, "Référentiel",
				"Fournisseurs, lots et données de base", 0, 1, List.of(
						new Node("nav.referential.providers", "Fournisseur",
								"Gestion des fournisseurs", 1, 0, List.of(
								new Node("nav.referential.providers.manage", "Ajouter/Modifier",
										"Création et modification de fiches fournisseur", 2, 0),
								new Node("nav.referential.providers.delete", "Supprimer",
										"Suppression de fiches fournisseur", 2, 1))),
						new Node("nav.referential.batches", "Lots",
								"Numéros de lot et péremption", 1, 1, List.of(
								new Node("nav.referential.batches.manage", "Ajouter/Modifier",
										"Création et mise à jour des lots", 2, 0),
								new Node("nav.referential.batches.delete", "Supprimer",
										"Suppression de lots", 2, 1))),
						new Node("nav.referential.masterdata", "Données de base",
								"Référentiels catalogue", 1, 2, List.of(
								new Node("nav.referential.masterdata.categories", "Catégories",
										"Familles de produits", 2, 0),
								new Node("nav.referential.masterdata.types", "Types",
										"Types de médicaments / DCI", 2, 1),
								new Node("nav.referential.masterdata.sections", "Rayons",
										"Rayons de vente", 2, 2),
								new Node("nav.referential.masterdata.thresholds", "Seuils",
										"Seuils d'alerte", 2, 3),
								new Node("nav.referential.masterdata.forms", "Formes",
										"Formes galéniques", 2, 4),
								new Node("nav.referential.masterdata.packaging", "Conditionnement",
										"Conditionnements / emballages", 2, 5)))));
	}

	private static Node organisationRoot() {
		return new Node(NavRoots.ORGANISATION, "Organisation",
				"Sites, magasins centraux et points de vente", 0, 2, List.of(
						new Node("nav.organisation.sites", "Sites",
								"Structure des sites", 1, 0, List.of(
										new Node("nav.organisation.sites.manage", "Ajouter/Modifier",
												"Création et mise à jour des sites", 2, 0),
										new Node("nav.organisation.sites.delete", "Supprimer",
												"Suppression de sites", 2, 1))),
						new Node("nav.organisation.central_stores", "Magasins Centraux",
								"Magasins centraux", 1, 1, List.of(
										new Node("nav.organisation.central_stores.manage", "Ajouter/Modifier",
												"Création et modification de magasins centraux", 2, 0),
										new Node("nav.organisation.central_stores.delete", "Supprimer",
												"Suppression de magasins centraux", 2, 1))),
						new Node("nav.organisation.sales_outlets", "Points de vente",
								"Points de vente", 1, 2, List.of(
										new Node("nav.organisation.sales_outlets.manage", "Ajouter/Modifier",
												"Création et modification des points de vente", 2, 0),
										new Node("nav.organisation.sales_outlets.delete", "Supprimer",
												"Suppression de points de vente", 2, 1)))));
	}

	private static Node approvisionnementRoot() {
		return new Node(NavRoots.APPROVISIONNEMENT, "Approvisionnement",
				"Produits, commandes et réception", 0, 3, List.of(
						new Node("nav.approvisionnement.products", "Produits",
								"Catalogue produits", 1, 0, List.of(
										new Node("nav.approvisionnement.products.manage", "Ajouter/Modifier",
												"Création et mise à jour des produits", 2, 0),
										new Node("nav.approvisionnement.products.delete", "Supprimer",
												"Suppression de produits", 2, 1))),
						new Node("nav.approvisionnement.orders", "Commandes",
								"Commandes fournisseurs", 1, 1, List.of(
										new Node("nav.approvisionnement.orders.manage", "Ajouter/Modifier",
												"Création et suivi des commandes", 2, 0),
										new Node("nav.approvisionnement.orders.delete", "Supprimer",
												"Suppression de commandes", 2, 1))),
						new Node("nav.approvisionnement.receipts", "Réception",
								"Réceptions marchandises", 1, 2, List.of(
										new Node("nav.approvisionnement.receipts.manage", "Ajouter/Modifier",
												"Saisie et mise à jour des réceptions", 2, 0),
										new Node("nav.approvisionnement.receipts.delete", "Supprimer",
												"Suppression de lignes de réception", 2, 1)))));
	}

	private static Node salesRoot() {
		return new Node(NavRoots.SALES, "Ventes",
				"Saisie ticket, caisse, historique et affectation vendeurs", 0, 4, List.of(
						new Node("nav.sales.ticket", "Vente",
								"Saisie et enregistrement des tickets", 1, 0, List.of(
										new Node("nav.sales.ticket.manage", "Ajouter/Modifier",
												"Saisie lignes et validation du ticket", 2, 0),
										new Node("nav.sales.ticket.print", "Imprimer",
												"Impression du ticket de vente", 2, 1))),
						new Node("nav.sales.cashdesk", "Caisse",
								"Encaissement et remboursement", 1, 1, List.of(
										new Node("nav.sales.cashdesk.read", "Consulter",
												"Chargement d’un ticket pour contrôle", 2, 0),
										new Node("nav.sales.cashdesk.collect", "Encaisser",
												"Enregistrement du paiement client", 2, 1),
										new Node("nav.sales.cashdesk.refund", "Rembourser",
												"Remboursement d’un ticket encaissé", 2, 2))),
						new Node("nav.sales.history", "Ventes",
								"Historique et consultation des tickets", 1, 2, List.of(
										new Node("nav.sales.history.read", "Consulter",
												"Recherche et affichage des ventes", 2, 0))),
						new Node("nav.sales.planning", "Affectation",
								"Planning et affectation des vendeurs", 1, 3, List.of(
										new Node("nav.sales.planning.manage", "Ajouter/Modifier",
												"Publication et mise à jour du planning", 2, 0)))));
	}

	private static Node stockRoot() {
		return new Node(NavRoots.STOCK, "Stock",
				"Stock central, stock PDV, mouvements et bons internes", 0, 5, List.of(
						new Node("nav.stock.central", "Stock central",
								"Quantités et ajustements magasin central", 1, 0, List.of(
										new Node("nav.stock.central.manage", "Ajouter/Modifier",
												"Création et mise à jour des lignes de stock", 2, 0),
										new Node("nav.stock.central.delete", "Supprimer",
												"Suppression de lignes de stock", 2, 1),
										new Node("nav.stock.central.adjust", "Ajuster",
												"Mouvements manuels (delta, type, commentaire)", 2, 2),
										new Node("nav.stock.central.internal_receipts", "Bons reçus",
												"Réception et traitement des bons internes", 2, 3, List.of(
												new Node("nav.stock.central.internal_receipts.receive", "Traiter",
														"Validation réception d’un bon interne", 3, 0))))),
						new Node("nav.stock.pdv", "Stock PDV",
								"Quantités et ajustements point de vente", 1, 1, List.of(
										new Node("nav.stock.pdv.manage", "Ajouter/Modifier",
												"Création et mise à jour des lignes de stock PDV", 2, 0),
										new Node("nav.stock.pdv.delete", "Supprimer",
												"Suppression de lignes de stock PDV", 2, 1),
										new Node("nav.stock.pdv.adjust", "Ajuster",
												"Mouvements manuels PDV", 2, 2),
										new Node("nav.stock.pdv.internal_orders", "Bons internes",
												"Création et suivi des bons vers le central", 2, 3, List.of(
												new Node("nav.stock.pdv.internal_orders.draft", "Brouillon",
														"Création d’un bon en brouillon", 3, 0),
												new Node("nav.stock.pdv.internal_orders.manage", "Ajouter/Modifier",
														"Édition des lignes d’un bon", 3, 1),
												new Node("nav.stock.pdv.internal_orders.send", "Envoyer",
														"Transmission du bon au magasin central", 3, 2),
												new Node("nav.stock.pdv.internal_orders.complete", "Clôturer",
														"Marquage du bon comme traité", 3, 3),
												new Node("nav.stock.pdv.internal_orders.cancel", "Annuler",
														"Annulation d’un bon en cours", 3, 4),
												new Node("nav.stock.pdv.internal_orders.delete", "Supprimer",
														"Suppression d’un brouillon", 3, 5))))),
						new Node("nav.stock.movements", "Mouvements",
								"Journal des mouvements de stock", 1, 2, List.of(
										new Node("nav.stock.movements.read", "Consulter",
												"Consultation filtrée des mouvements", 2, 0)))));
	}

	private static Node pilotageRoot() {
		return new Node(NavRoots.PILOTAGE, "Pilotage",
				"Tableau de bord et indicateurs de pilotage", 0, 6, List.of(
						new Node("nav.pilotage.dashboard", "Tableau de bord",
								"Synthèse CA, marges et indicateurs", 1, 0, List.of(
										new Node("nav.pilotage.dashboard.read", "Consulter",
												"Consultation des KPI et filtres période/site", 2, 0)))));
	}
}
