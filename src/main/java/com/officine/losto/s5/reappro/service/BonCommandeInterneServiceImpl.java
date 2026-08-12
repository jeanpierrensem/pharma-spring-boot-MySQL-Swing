package com.officine.losto.s5.reappro.service;

import com.officine.losto.entity.Batch;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.LigneBonCommandeInterne;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.model.BatchRepo;
import com.officine.losto.s5.reappro.S5Specifications;
import com.officine.losto.s5.reappro.dto.BatchLivraisonDto;
import com.officine.losto.s5.reappro.dto.BonTraitementMagasinRequestDto;
import com.officine.losto.s5.reappro.dto.LigneTraitementMagasinDto;
import com.officine.losto.s5.reappro.repository.BonCommandeInterneRepository;
import com.officine.losto.s5.reappro.security.BonCommandeInterneWarehouseAuthPolicy;
import com.officine.losto.s7.stocks.domain.ReferenceStockType;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BonCommandeInterneServiceImpl {

	private final BonCommandeInterneRepository repo;
	private final StockCentralRepository stockCentralRepository;
	private final StockPdvRepository stockPdvRepository;
	private final MouvementStockRepository mouvementStockRepository;
	private final BatchRepo batchRepo;
	private final BonCommandeInterneWarehouseAuthPolicy warehouseAuthPolicy;

	@Transactional(readOnly = true)
	public List<BonCommandeInterne> getAll() {
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public BonCommandeInterne loadById(long id) {
		return repo.findById(id).orElse(null);
	}

	/**
	 * Création : toujours {@link StatutBonCommandeInterne#BROUILLON}. Mise à jour : passer le statut
	 * d’origine via {@link #save(BonCommandeInterne, StatutBonCommandeInterne)}.
	 */
	@Transactional
	public BonCommandeInterne save(BonCommandeInterne b) {
		return save(b, null);
	}

	/**
	 * @param statutAvantFusion statut persisté avant fusion DTO (pour valider les transitions en mise à jour ;
	 *                          ignoré si {@code b.getId()} est null).
	 */
	@Transactional
	public BonCommandeInterne save(BonCommandeInterne b, StatutBonCommandeInterne statutAvantFusion) {
		if (b.getId() == null) {
			b.setStatut(StatutBonCommandeInterne.BROUILLON);
			if (b.getNumber() == null || b.getNumber().isBlank()) {
				b.setNumber(genererNumeroBonInterne(b));
			}
			validateLineContents(b.getLignes());
			return repo.save(b);
		}

		StatutBonCommandeInterne ancienStatut =
				statutAvantFusion != null ? statutAvantFusion : StatutBonCommandeInterne.BROUILLON;
		StatutBonCommandeInterne nouveauStatut =
				b.getStatut() != null ? b.getStatut() : ancienStatut;

		assertSaveUpdateAllowed(ancienStatut, nouveauStatut);
		warehouseAuthPolicy.assertWarehouseDecisionAllowed(ancienStatut, nouveauStatut);

		ancienStatut.validateTransitionTo(nouveauStatut);
		b.setStatut(nouveauStatut);

		validateStatutRequiresLines(nouveauStatut, b.getLignes());
		validateLineContents(b.getLignes());

		return repo.save(b);
	}

	@Transactional
	public void remove(BonCommandeInterne b) {
		if (b == null || b.getId() == null) {
			return;
		}
		BonCommandeInterne fresh = repo.findById(b.getId()).orElse(null);
		if (fresh == null) {
			return;
		}
		if (fresh.getStatut() != StatutBonCommandeInterne.BROUILLON) {
			throw new IllegalArgumentException(
					"Suppression réservée aux bons en BROUILLON (statut actuel : " + fresh.getStatut() + ")");
		}
		repo.delete(fresh);
	}

	@Transactional(readOnly = true)
	public List<BonCommandeInterne> findFiltered(
			Long siteId,
			Long pointDeVenteId,
			Long magasinCentralId,
			StatutBonCommandeInterne statut,
			List<StatutBonCommandeInterne> statuts,
			LocalDate from,
			LocalDate to) {
		return repo.findAll(
				S5Specifications.filterBon(
						siteId, pointDeVenteId, magasinCentralId, statut, statuts, from, to));
	}

	/** Bons encore à traiter au magasin central (ENVOYE ou livraison PARTIEL en cours). */
	@Transactional(readOnly = true)
	public List<BonCommandeInterne> findEnCoursTraitementMagasin(long magasinCentralId) {
		return findFiltered(
				null,
				null,
				magasinCentralId,
				null,
				List.of(StatutBonCommandeInterne.ENVOYE, StatutBonCommandeInterne.PARTIEL),
				null,
				null);
	}

	/**
	 * Traitement magasin central : livraison (incrémentale si PARTIEL), statut PARTIEL si au moins une
	 * ligne a une quantité livrée cumulée strictement inférieure à la quantité commandée.
	 */
	@Transactional
	public BonCommandeInterne traiterAuMagasinCentral(BonTraitementMagasinRequestDto request) {
		if (request == null || request.getBonId() == null) {
			throw new IllegalArgumentException("Identifiant de bon obligatoire");
		}
		BonCommandeInterne bon =
				repo.findById(request.getBonId())
						.orElseThrow(() -> new IllegalArgumentException("Bon introuvable : " + request.getBonId()));
		StatutBonCommandeInterne ancien = bon.getStatut();
		if (ancien != StatutBonCommandeInterne.ENVOYE && ancien != StatutBonCommandeInterne.PARTIEL) {
			throw new IllegalArgumentException(
					"Seuls les bons ENVOYE ou PARTIEL peuvent être traités au magasin central (statut actuel : "
							+ ancien
							+ ")");
		}

		List<LigneBonCommandeInterne> lignesBon =
				bon.getLignes() == null ? Collections.emptyList() : bon.getLignes();
		if (request.getLines() == null || request.getLines().size() != lignesBon.size()) {
			throw new IllegalArgumentException("Toutes les lignes du bon doivent être traitées");
		}
		Map<Long, LigneBonCommandeInterne> byId = new HashMap<>();
		for (LigneBonCommandeInterne l : lignesBon) {
			if (l.getId() != null) {
				byId.put(l.getId(), l);
			}
		}

		boolean livraisonCettePasse = false;
		for (LigneTraitementMagasinDto lt : request.getLines()) {
			LigneBonCommandeInterne ligne = byId.get(lt.getLineId());
			if (ligne == null) {
				throw new IllegalArgumentException("Ligne de bon inconnue : " + lt.getLineId());
			}
			int ordered = nz(ligne.getQuantity());
			int alreadyDelivered = nz(ligne.getQuantityDelivered());
			int increment = lt.getQuantityDelivered() == null ? 0 : lt.getQuantityDelivered();
			if (increment > 0) {
				livraisonCettePasse = true;
			}
			int newTotalDelivered = alreadyDelivered + increment;
			if (increment < 0 || newTotalDelivered > ordered) {
				throw new IllegalArgumentException(
						"Quantité livrée invalide pour la ligne "
								+ lt.getLineId()
								+ " : cumul="
								+ newTotalDelivered
								+ ", commandé="
								+ ordered);
			}

			Product product = ligne.getProduct();
			if (product == null || product.getId() == null) {
				throw new IllegalArgumentException("Ligne sans produit : " + lt.getLineId());
			}

			MagasinCentral magasinCentral = bon.getMagasinCentral();
			PointDeVente pdv = bon.getPointDeVente();
			if (magasinCentral == null || pdv == null) {
				throw new IllegalArgumentException("Bon incomplet : magasin central ou PDV manquant.");
			}

			List<BatchLivraisonDto> allocations =
					lt.getBatchAllocations() == null ? Collections.emptyList() : lt.getBatchAllocations();
			if (increment > 0) {
				if (allocations.isEmpty()) {
					throw new IllegalArgumentException(
							"Au moins un lot est requis pour livrer la ligne " + lt.getLineId());
				}
				int sumAlloc = 0;
				for (BatchLivraisonDto alloc : allocations) {
					sumAlloc += nz(alloc.getQuantity());
				}
				if (sumAlloc != increment) {
					throw new IllegalArgumentException(
							"La somme des quantités par lot doit égaler la quantité livrée (ligne "
									+ lt.getLineId()
									+ ")");
				}
				for (BatchLivraisonDto alloc : allocations) {
					int allocQty = nz(alloc.getQuantity());
					if (allocQty <= 0 || alloc.getBatchId() == null) {
						continue;
					}
					int dispoLot =
							stockCentralRepository
									.findByMagasinCentral_IdAndProduct_IdAndBatch_Id(
											magasinCentral.getId(), product.getId(), alloc.getBatchId())
									.map(r -> nz(r.getQteDisponible()))
									.orElse(0);
					if (allocQty > dispoLot) {
						throw new IllegalArgumentException(
								String.format(
										"Stock central insuffisant pour produit %s lot %s : dispo=%d, livraison demandée=%d",
										product.getId(), alloc.getBatchId(), dispoLot, allocQty));
					}
				}
				appliquerLivraisonLigne(bon, ligne, pdv, magasinCentral, allocations);
			}

			ligne.setQuantityDelivered(newTotalDelivered);
			if (!allocations.isEmpty()) {
				Batch first =
						batchRepo.findById(allocations.get(0).getBatchId()).orElse(null);
				ligne.setBatch(first);
			}
		}

		StatutBonCommandeInterne nouveauStatut =
				resolveStatutApresTraitement(ancien, lignesBon, livraisonCettePasse);
		warehouseAuthPolicy.assertWarehouseDecisionAllowed(ancien, nouveauStatut);
		ancien.validateTransitionTo(nouveauStatut);
		bon.setStatut(nouveauStatut);
		return repo.save(bon);
	}

	/**
	 * TRAITE si toutes les lignes sont livrées intégralement ; PARTIEL si au moins une quantité livrée
	 * cumulée est strictement inférieure à la quantité commandée et qu'une livraison a eu lieu (cette passe
	 * ou précédente) ; sinon ENVOYE (aucune livraison encore).
	 */
	private static StatutBonCommandeInterne resolveStatutApresTraitement(
			StatutBonCommandeInterne ancien,
			List<LigneBonCommandeInterne> lignes,
			boolean livraisonCettePasse) {
		boolean traitementTotal = true;
		for (LigneBonCommandeInterne l : lignes) {
			if (nz(l.getQuantityDelivered()) < nz(l.getQuantity())) {
				traitementTotal = false;
				break;
			}
		}
		if (traitementTotal) {
			return StatutBonCommandeInterne.TRAITE;
		}
		boolean dejaLivre =
				livraisonCettePasse
						|| ancien == StatutBonCommandeInterne.PARTIEL
						|| lignes.stream().anyMatch(l -> nz(l.getQuantityDelivered()) > 0);
		if (dejaLivre) {
			return StatutBonCommandeInterne.PARTIEL;
		}
		return StatutBonCommandeInterne.ENVOYE;
	}

	private void appliquerLivraisonLigne(
			BonCommandeInterne bon,
			LigneBonCommandeInterne ligne,
			PointDeVente pdv,
			MagasinCentral magasinCentral,
			List<BatchLivraisonDto> allocations) {
		Product product = ligne.getProduct();
		Long bonId = bon.getId();
		String numero = bon.getNumber() == null ? "?" : bon.getNumber();
		LocalDateTime now = LocalDateTime.now();
		int totalDelivered = 0;

		for (BatchLivraisonDto alloc : allocations) {
			int qty = nz(alloc.getQuantity());
			if (qty <= 0) {
				continue;
			}
			totalDelivered += qty;
			Batch batch =
					batchRepo.findById(alloc.getBatchId())
							.orElseThrow(
									() -> new IllegalArgumentException("Lot introuvable : " + alloc.getBatchId()));
			String lotLabel =
					batch.getNumber() != null && !batch.getNumber().isBlank()
							? batch.getNumber()
							: "id=" + batch.getId();

			StockCentral stockCentral =
					stockCentralRepository
							.findByMagasinCentral_IdAndProduct_IdAndBatch_Id(
									magasinCentral.getId(), product.getId(), batch.getId())
							.orElseThrow(
									() ->
											new IllegalArgumentException(
													"Aucune ligne stock central pour produit "
															+ product.getId()
															+ " lot "
															+ lotLabel));
			int dispo = nz(stockCentral.getQteDisponible());
			if (qty > dispo) {
				throw new IllegalArgumentException(
						String.format(
								"Stock central insuffisant produit %s lot %s : dispo=%d demandé=%d",
								product.getId(), lotLabel, dispo, qty));
			}
			stockCentral.setQteDisponible(dispo - qty);
			stockCentral.setUpdatedAt(now);
			stockCentralRepository.save(stockCentral);

			mouvementStockRepository.save(
					buildMouvementStock(
							product,
							batch,
							TypeMouvementStock.SORTIE,
							-qty,
							magasinCentral.getSite(),
							null,
							bonId,
							"SORTIE centrale bon interne n° "
									+ numero
									+ " produit "
									+ product.getId()
									+ " lot "
									+ lotLabel));

			mouvementStockRepository.save(
					buildMouvementStock(
							product,
							batch,
							TypeMouvementStock.ENTREE,
							qty,
							pdv.getSite(),
							pdv,
							bonId,
							"ENTREE PDV bon interne n° "
									+ numero
									+ " produit "
									+ product.getId()
									+ " lot "
									+ lotLabel));
		}

		StockPdv stockPdv =
				stockPdvRepository
						.findByPointDeVente_IdAndProduct_Id(pdv.getId(), product.getId())
						.orElseGet(
								() ->
										StockPdv.builder()
												.pointDeVente(pdv)
												.product(product)
												.qteDisponible(0)
												.qteReservee(0)
												.build());
		stockPdv.setQteDisponible(nz(stockPdv.getQteDisponible()) + totalDelivered);
		stockPdv.setUpdatedAt(now);
		stockPdvRepository.save(stockPdv);
	}

	/**
	 * Mise à jour via {@link #save} : réservée au PDV en BROUILLON, sauf annulation magasin central
	 * (ENVOYE ou PARTIEL → ANNULE). Le traitement livraison passe par {@link #traiterAuMagasinCentral}.
	 */
	private static void assertSaveUpdateAllowed(
			StatutBonCommandeInterne ancienStatut, StatutBonCommandeInterne nouveauStatut) {
		if (ancienStatut == StatutBonCommandeInterne.BROUILLON) {
			return;
		}
		if ((ancienStatut == StatutBonCommandeInterne.ENVOYE
						|| ancienStatut == StatutBonCommandeInterne.PARTIEL)
				&& nouveauStatut == StatutBonCommandeInterne.ANNULE) {
			return;
		}
		throw new IllegalArgumentException(
				"Modification réservée aux bons en BROUILLON (statut actuel : " + ancienStatut + ")");
	}

	private static MouvementStock buildMouvementStock(
			Product product,
			Batch batch,
			TypeMouvementStock type,
			int qtyAlgebra,
			com.officine.losto.entity.Site site,
			PointDeVente pdv,
			Long bonId,
			String commentaire) {
		return MouvementStock.builder()
				.product(product)
				.batch(batch)
				.typeMouvement(type)
				.quantiteAlgebrique(qtyAlgebra)
				.referenceType(ReferenceStockType.BON_INTERNE)
				.referenceId(bonId)
				.site(site)
				.pointDeVente(pdv)
				.dateMouvement(LocalDateTime.now())
				.commentaire(commentaire.length() <= 512 ? commentaire : commentaire.substring(0, 512))
				.build();
	}

	private static void validateStatutRequiresLines(
			StatutBonCommandeInterne statut, List<LigneBonCommandeInterne> lignes) {
		List<LigneBonCommandeInterne> safe = lignes == null ? Collections.emptyList() : lignes;
		if (statut == StatutBonCommandeInterne.ENVOYE
				|| statut == StatutBonCommandeInterne.TRAITE
				|| statut == StatutBonCommandeInterne.PARTIEL) {
			if (safe.isEmpty()) {
				throw new IllegalArgumentException(
						"Les statuts ENVOYE et TRAITE exigent au moins une ligne de bon");
			}
		}
	}

	private static void validateLineContents(List<LigneBonCommandeInterne> lignes) {
		if (lignes == null) {
			return;
		}
		for (LigneBonCommandeInterne l : lignes) {
			if (l.getQuantity() == null || l.getQuantity() <= 0) {
				throw new IllegalArgumentException("Quantité de ligne > 0 requise");
			}
			if (l.getUnitPrice() != null && l.getUnitPrice().signum() < 0) {
				throw new IllegalArgumentException("Prix unitaire négatif interdit");
			}
		}
	}

	private static int nz(Integer v) {
		return v == null ? 0 : v;
	}

	/**
	 * Numéro de bon à la création : identifiant PDV + date (yyyyMMdd) + horodatage ms (colonne {@code NUMBER}, max 64).
	 */
	static String genererNumeroBonInterne(BonCommandeInterne bon) {
		PointDeVente pdv = bon.getPointDeVente();
		if (pdv == null || pdv.getId() == null) {
			throw new IllegalArgumentException(
					"Point de vente obligatoire pour générer le numéro de bon interne.");
		}
		LocalDate date = bon.getOrderDate() != null ? bon.getOrderDate() : LocalDate.now();
		return pdv.getId()
				+ date.format(DateTimeFormatter.BASIC_ISO_DATE)
				+ System.currentTimeMillis();
	}
}
