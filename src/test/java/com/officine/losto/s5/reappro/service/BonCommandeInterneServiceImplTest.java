package com.officine.losto.s5.reappro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.officine.losto.entity.Batch;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.LigneBonCommandeInterne;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Site;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.model.BatchRepo;
import com.officine.losto.s5.reappro.dto.BatchLivraisonDto;
import com.officine.losto.s5.reappro.dto.BonTraitementMagasinRequestDto;
import com.officine.losto.s5.reappro.dto.LigneTraitementMagasinDto;
import com.officine.losto.s5.reappro.repository.BonCommandeInterneRepository;
import com.officine.losto.s5.reappro.security.BonCommandeInterneWarehouseAuthPolicy;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BonCommandeInterneServiceImplTest {

	@Mock
	private BonCommandeInterneRepository repo;

	@Mock
	private StockCentralRepository stockCentralRepository;

	@Mock
	private StockPdvRepository stockPdvRepository;

	@Mock
	private MouvementStockRepository mouvementStockRepository;

	@Mock
	private BatchRepo batchRepo;

	@Mock
	private BonCommandeInterneWarehouseAuthPolicy warehouseAuthPolicy;

	@InjectMocks
	private BonCommandeInterneServiceImpl service;

	@Test
	void save_create_forcesBrouillon_andAllowsEmptyLines() {
		PointDeVente pdv = PointDeVente.builder().build();
		pdv.setId(42L);
		BonCommandeInterne draft = BonCommandeInterne.builder()
				.orderDate(LocalDate.of(2026, 5, 21))
				.pointDeVente(pdv)
				.lignes(new ArrayList<>())
				.statut(StatutBonCommandeInterne.ENVOYE)
				.build();
		when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

		BonCommandeInterne saved = service.save(draft);

		assertThat(saved.getStatut()).isEqualTo(StatutBonCommandeInterne.BROUILLON);
		assertThat(saved.getNumber())
				.isNotBlank()
				.startsWith("42")
				.contains("20260521")
				.matches("42\\d{8}\\d{13}");
		verify(repo).save(draft);
		verify(stockCentralRepository, times(0)).save(any());
	}

	@Test
	void save_create_withoutPdv_throwsWhenGeneratingNumber() {
		BonCommandeInterne draft = BonCommandeInterne.builder()
				.orderDate(LocalDate.now())
				.lignes(new ArrayList<>())
				.build();

		assertThatThrownBy(() -> service.save(draft))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Point de vente");
	}

	@Test
	void save_create_preservesProvidedNumber() {
		BonCommandeInterne draft = BonCommandeInterne.builder()
				.number("BINT-MANUAL-001")
				.orderDate(LocalDate.now())
				.lignes(new ArrayList<>())
				.build();
		when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

		BonCommandeInterne saved = service.save(draft);

		assertThat(saved.getNumber()).isEqualTo("BINT-MANUAL-001");
	}

	@Test
	void save_create_generatesNumberWhenBlank() {
		PointDeVente pdv = PointDeVente.builder().build();
		pdv.setId(7L);
		BonCommandeInterne draft = BonCommandeInterne.builder()
				.number("   ")
				.orderDate(LocalDate.of(2026, 1, 15))
				.pointDeVente(pdv)
				.lignes(new ArrayList<>())
				.build();
		when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

		BonCommandeInterne saved = service.save(draft);

		assertThat(saved.getNumber())
				.isNotBlank()
				.startsWith("7")
				.contains("20260115")
				.matches("7\\d{8}\\d{13}");
	}

	@Test
	void save_create_validatesPresentLines() {
		BonCommandeInterne draft = BonCommandeInterne.builder()
				.lignes(List.of(line(null, null, Product.builder().build(), 0, BigDecimal.ONE, null)))
				.build();
		assertThatThrownBy(() -> service.save(draft)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void save_update_validatesTransition() {
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(1L)
				.statut(StatutBonCommandeInterne.TRAITE)
				.lignes(List.of(line(null, null, Product.builder().build(), 1, BigDecimal.ZERO, null)))
				.build();

		assertThatThrownBy(() -> service.save(bon, StatutBonCommandeInterne.BROUILLON))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("BROUILLON");
	}

	@Test
	void save_update_envoye_requiresAtLeastOneLine() {
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(1L)
				.statut(StatutBonCommandeInterne.ENVOYE)
				.lignes(new ArrayList<>())
				.build();

		assertThatThrownBy(() -> service.save(bon, StatutBonCommandeInterne.BROUILLON))
				.hasMessageContaining("au moins une ligne");
	}

	@Test
	void save_update_brouillonVersEnvoye_ok() {
		List<LigneBonCommandeInterne> lignes =
				List.of(line(null, null, Product.builder().build(), 2, new BigDecimal("3.50"), null));
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(1L)
				.statut(StatutBonCommandeInterne.ENVOYE)
				.lignes(lignes)
				.build();
		when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

		BonCommandeInterne out = service.save(bon, StatutBonCommandeInterne.BROUILLON);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.ENVOYE);
		verify(repo).save(bon);
	}

	@Test
	void save_update_envoyeToTraite_rejected() {
		Product prod = Product.builder().build();
		prod.setId(10L);
		List<LigneBonCommandeInterne> lignes =
				List.of(line(null, null, prod, 2, BigDecimal.ONE, Batch.builder().build()));
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(1L)
				.statut(StatutBonCommandeInterne.TRAITE)
				.lignes(lignes)
				.build();

		assertThatThrownBy(() -> service.save(bon, StatutBonCommandeInterne.ENVOYE))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("BROUILLON");

		verify(repo, times(0)).save(any(BonCommandeInterne.class));
	}

	@Test
	void save_update_partiel_rejected() {
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(1L)
				.statut(StatutBonCommandeInterne.PARTIEL)
				.lignes(List.of(line(null, null, Product.builder().build(), 1, BigDecimal.ZERO, null)))
				.build();

		assertThatThrownBy(() -> service.save(bon, StatutBonCommandeInterne.PARTIEL))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("BROUILLON");
	}

	@Test
	void save_update_envoyeToAnnule_ok() {
		List<LigneBonCommandeInterne> lignes =
				List.of(line(null, null, Product.builder().build(), 2, new BigDecimal("3.50"), null));
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(1L)
				.statut(StatutBonCommandeInterne.ANNULE)
				.lignes(lignes)
				.build();
		when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

		BonCommandeInterne out = service.save(bon, StatutBonCommandeInterne.ENVOYE);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.ANNULE);
		verify(repo).save(bon);
		verify(stockCentralRepository, times(0)).save(any());
	}

	@Test
	void remove_onlyBrouillon() {
		BonCommandeInterne b = BonCommandeInterne.builder()
				.id(9L)
				.statut(StatutBonCommandeInterne.BROUILLON)
				.build();
		when(repo.findById(9L)).thenReturn(Optional.of(b));

		service.remove(b);

		verify(repo).delete(b);
	}

	@Test
	void remove_nonBrouillon_throws() {
		BonCommandeInterne fresh = BonCommandeInterne.builder()
				.id(9L)
				.statut(StatutBonCommandeInterne.ENVOYE)
				.build();
		when(repo.findById(9L)).thenReturn(Optional.of(fresh));

		assertThatThrownBy(() -> service.remove(BonCommandeInterne.builder().id(9L).build()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("BROUILLON");

		verify(repo).findById(9L);
	}

	@Test
	void traiterAuMagasinCentral_zeroDelivery_staysEnvoye() {
		BonCommandeInterne bon = traitementBonEnvoye(10, 0, 100);
		when(repo.findById(bon.getId())).thenReturn(Optional.of(bon));
		when(repo.save(any(BonCommandeInterne.class))).thenAnswer(inv -> inv.getArgument(0));

		BonTraitementMagasinRequestDto request =
				BonTraitementMagasinRequestDto.builder()
						.bonId(77L)
						.lines(
								List.of(
										LigneTraitementMagasinDto.builder()
												.lineId(1L)
												.quantityDelivered(0)
												.batchAllocations(List.of())
												.build()))
						.build();

		BonCommandeInterne out = service.traiterAuMagasinCentral(request);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.ENVOYE);
	}

	@Test
	void traiterAuMagasinCentral_zeroDeliveryOnPartiel_staysPartiel() {
		BonCommandeInterne bon = traitementBonEnvoye(10, 6, 100);
		bon.setStatut(StatutBonCommandeInterne.PARTIEL);
		when(repo.findById(bon.getId())).thenReturn(Optional.of(bon));
		when(repo.save(any(BonCommandeInterne.class))).thenAnswer(inv -> inv.getArgument(0));

		BonTraitementMagasinRequestDto request =
				BonTraitementMagasinRequestDto.builder()
						.bonId(77L)
						.lines(
								List.of(
										LigneTraitementMagasinDto.builder()
												.lineId(1L)
												.quantityDelivered(0)
												.batchAllocations(List.of())
												.build()))
						.build();

		BonCommandeInterne out = service.traiterAuMagasinCentral(request);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.PARTIEL);
	}

	@Test
	void traiterAuMagasinCentral_partielWhenDeliveredLessThanOrdered() {
		BonCommandeInterne bon = traitementBonEnvoye(10, 0, 100);
		LigneBonCommandeInterne ligne = bon.getLignes().get(0);
		stubStockCentral(bon, ligne.getProduct(), 100);
		when(batchRepo.findById(505L)).thenReturn(Optional.of(ligne.getBatch()));
		when(repo.save(any(BonCommandeInterne.class))).thenAnswer(inv -> inv.getArgument(0));

		BonTraitementMagasinRequestDto request =
				BonTraitementMagasinRequestDto.builder()
						.bonId(77L)
						.lines(
								List.of(
										LigneTraitementMagasinDto.builder()
												.lineId(1L)
												.quantityDelivered(6)
												.batchAllocations(
														List.of(
																BatchLivraisonDto.builder()
																		.batchId(505L)
																		.quantity(6)
																		.build()))
												.build()))
						.build();

		BonCommandeInterne out = service.traiterAuMagasinCentral(request);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.PARTIEL);
		assertThat(ligne.getQuantityDelivered()).isEqualTo(6);
	}

	@Test
	void traiterAuMagasinCentral_partielWhenStockLimitsDeliveryBelowOrdered() {
		BonCommandeInterne bon = traitementBonEnvoye(10, 0, 100);
		LigneBonCommandeInterne ligne = bon.getLignes().get(0);
		stubStockCentral(bon, ligne.getProduct(), 5);
		when(batchRepo.findById(505L)).thenReturn(Optional.of(ligne.getBatch()));
		when(repo.save(any(BonCommandeInterne.class))).thenAnswer(inv -> inv.getArgument(0));

		BonTraitementMagasinRequestDto request =
				BonTraitementMagasinRequestDto.builder()
						.bonId(77L)
						.lines(
								List.of(
										LigneTraitementMagasinDto.builder()
												.lineId(1L)
												.quantityDelivered(5)
												.batchAllocations(
														List.of(
																BatchLivraisonDto.builder()
																		.batchId(505L)
																		.quantity(5)
																		.build()))
												.build()))
						.build();

		BonCommandeInterne out = service.traiterAuMagasinCentral(request);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.PARTIEL);
		assertThat(ligne.getQuantityDelivered()).isEqualTo(5);
	}

	@Test
	void traiterAuMagasinCentral_cumulativeCompletionFromPartiel() {
		BonCommandeInterne bon = traitementBonEnvoye(10, 6, 100);
		bon.setStatut(StatutBonCommandeInterne.PARTIEL);
		LigneBonCommandeInterne ligne = bon.getLignes().get(0);
		stubStockCentral(bon, ligne.getProduct(), 100);
		when(batchRepo.findById(505L)).thenReturn(Optional.of(ligne.getBatch()));
		when(repo.save(any(BonCommandeInterne.class))).thenAnswer(inv -> inv.getArgument(0));

		BonTraitementMagasinRequestDto request =
				BonTraitementMagasinRequestDto.builder()
						.bonId(77L)
						.lines(
								List.of(
										LigneTraitementMagasinDto.builder()
												.lineId(1L)
												.quantityDelivered(4)
												.batchAllocations(
														List.of(
																BatchLivraisonDto.builder()
																		.batchId(505L)
																		.quantity(4)
																		.build()))
												.build()))
						.build();

		BonCommandeInterne out = service.traiterAuMagasinCentral(request);

		assertThat(out.getStatut()).isEqualTo(StatutBonCommandeInterne.TRAITE);
		assertThat(ligne.getQuantityDelivered()).isEqualTo(10);
	}

	private static BonCommandeInterne traitementBonEnvoye(int ordered, int alreadyDelivered, int centralStock) {
		return traitementBonSkeleton(
				List.of(line(1L, null, product(10L), ordered, BigDecimal.ONE, batch(505L))),
				StatutBonCommandeInterne.ENVOYE,
				alreadyDelivered);
	}

	private void stubStockCentral(BonCommandeInterne bon, Product product, int dispo) {
		MagasinCentral mc = bon.getMagasinCentral();
		Batch batch = bon.getLignes().isEmpty() ? null : bon.getLignes().get(0).getBatch();
		StockCentral stockCentralRow =
				StockCentral.builder()
						.magasinCentral(mc)
						.site(mc.getSite())
						.product(product)
						.batch(batch)
						.qteDisponible(dispo)
						.qteReservee(0)
						.build();
		when(stockCentralRepository.findByMagasinCentral_IdAndProduct_IdAndBatch_Id(
						mc.getId(), product.getId(), batch == null ? null : batch.getId()))
				.thenReturn(Optional.of(stockCentralRow));
		when(stockPdvRepository.findByPointDeVente_IdAndProduct_Id(
						bon.getPointDeVente().getId(), product.getId()))
				.thenReturn(Optional.empty());
		when(repo.findById(bon.getId())).thenReturn(Optional.of(bon));
	}

	private static Product product(long id) {
		Product p = Product.builder().build();
		p.setId(id);
		return p;
	}

	private static Batch batch(long id) {
		Batch b = Batch.builder().number("LOT-2026-A").build();
		b.setId(id);
		return b;
	}

	private static BonCommandeInterne traitementBonSkeleton(List<LigneBonCommandeInterne> lignes) {
		return traitementBonSkeleton(lignes, StatutBonCommandeInterne.TRAITE, 0);
	}

	private static BonCommandeInterne traitementBonSkeleton(
			List<LigneBonCommandeInterne> lignes,
			StatutBonCommandeInterne statut,
			int quantityDelivered) {
		Site s = new Site();
		s.setId(900L);

		PointDeVente pdv = PointDeVente.builder().site(new Site()).build();
		pdv.setId(11L);

		MagasinCentral mc = MagasinCentral.builder().site(s).build();
		mc.setId(3L);

		for (LigneBonCommandeInterne l : lignes) {
			l.setQuantityDelivered(quantityDelivered);
		}

		BonCommandeInterne bon =
				BonCommandeInterne.builder()
						.id(77L)
						.statut(statut)
						.lignes(lignes)
						.magasinCentral(mc)
						.pointDeVente(pdv)
						.build();
		for (LigneBonCommandeInterne l : lignes) {
			l.setBon(bon);
		}
		return bon;
	}

	private static LigneBonCommandeInterne line(
			Long ligneId,
			BonCommandeInterne bon,
			Product product,
			int qty,
			BigDecimal unit,
			Batch batch) {
		LigneBonCommandeInterne l = LigneBonCommandeInterne.builder()
				.bon(bon)
				.product(product)
				.quantity(qty)
				.unitPrice(unit)
				.batch(batch)
				.build();
		l.setId(ligneId);
		return l;
	}
}
