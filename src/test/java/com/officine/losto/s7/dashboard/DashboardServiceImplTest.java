package com.officine.losto.s7.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.Site;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl;
import com.officine.losto.s7.dashboard.repository.DashboardBonQueryRepository;
import com.officine.losto.s7.dashboard.repository.DashboardQueryRepository;
import com.officine.losto.s7.pilotage.PilotageProduitSort;
import com.officine.losto.s7.pilotage.PilotageVenteService;
import com.officine.losto.s7.pilotage.dto.ClassementProduit;
import com.officine.losto.s7.pilotage.dto.PeriodeFiltre;
import com.officine.losto.s7.pilotage.dto.ResultatCaParPdv;
import com.officine.losto.s7.stocks.repository.MouvementStockRepository;
import com.officine.losto.s7.stocks.repository.StockCentralRepository;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

	@Mock
	private PilotageVenteService pilotageVenteService;
	@Mock
	private DashboardQueryRepository dashboardQueryRepository;
	@Mock
	private DashboardBonQueryRepository dashboardBonQueryRepository;
	@Mock
	private BonCommandeInterneServiceImpl bonCommandeInterneService;
	@Mock
	private MagasinCentralRepository magasinCentralRepository;
	@Mock
	private StockPdvRepository stockPdvRepository;
	@Mock
	private StockCentralRepository stockCentralRepository;
	@Mock
	private MouvementStockRepository mouvementStockRepository;

	@InjectMocks
	private DashboardServiceImpl dashboardService;

	@Test
	void synthese_agregatesKpisAndAlerts() {
		LocalDate from = LocalDate.of(2026, 4, 1);
		LocalDate to = LocalDate.of(2026, 4, 30);
		long siteId = 10L;

		when(pilotageVenteService.caParPointDeVente(any(PeriodeFiltre.class)))
				.thenReturn(List.of(ResultatCaParPdv.builder()
						.pointDeVenteId(1L)
						.libellePdv("Caisse")
						.chiffreAffaires(new BigDecimal("100.00"))
						.build()));
		when(dashboardQueryRepository.countTickets(from, to, siteId)).thenReturn(5L);
		when(dashboardQueryRepository.caEtCout(from, to, siteId))
				.thenReturn(List.<Object[]>of(new Object[] { new BigDecimal("100"), new BigDecimal("60") }));
		when(dashboardQueryRepository.caParJour(from, to, siteId)).thenReturn(List.of());
		when(dashboardQueryRepository.dernieresVentes(eq(from), eq(to), eq(siteId), any(Pageable.class)))
				.thenReturn(List.of());
		when(pilotageVenteService.topProduitsParCa(any(PeriodeFiltre.class), eq(5), eq(PilotageProduitSort.CA)))
				.thenReturn(List.of(ClassementProduit.builder()
						.rang(1)
						.productId(99L)
						.libelle("Paracétamol")
						.chiffreAffaires(new BigDecimal("100.00"))
						.quantiteVendue(10L)
						.build()));
		when(stockPdvRepository.sumQteDisponibleByProductAndSite(99L, siteId)).thenReturn(12);

		MagasinCentral mc = MagasinCentral.builder().id(7L).libelle("MC Paris").build();
		when(magasinCentralRepository.findBySite_Id(siteId)).thenReturn(Optional.of(mc));

		Site site = Site.builder().id(siteId).code("PARIS").libelle("Paris").build();
		PointDeVente pdv = PointDeVente.builder().id(1L).site(site).libelle("Caisse").build();
		BonCommandeInterne bon = BonCommandeInterne.builder()
				.id(3L)
				.number("BINT-001")
				.orderDate(LocalDate.now().minusDays(5))
				.statut(StatutBonCommandeInterne.ENVOYE)
				.pointDeVente(pdv)
				.build();
		when(bonCommandeInterneService.findEnCoursTraitementMagasin(7L)).thenReturn(List.of(bon));
		when(dashboardBonQueryRepository.bonsParJour(from, to, siteId, StatutBonCommandeInterne.TRAITE))
				.thenReturn(List.of());
		when(mouvementStockRepository.findBySite_IdOrderByDateMouvementDesc(siteId)).thenReturn(List.of());

		Product product = Product.builder().id(99L).name("Vitamine D").build();
		StockPdv stockBas = StockPdv.builder()
				.id(50L)
				.pointDeVente(pdv)
				.product(product)
				.qteDisponible(2)
				.qteSeuilAlerte(5)
				.build();
		when(stockPdvRepository.findForDashboard(siteId)).thenReturn(List.of(stockBas));
		when(stockCentralRepository.findBySite_Id(siteId)).thenReturn(List.of());

		var result = dashboardService.synthese(from, to, siteId);

		assertThat(result.getKpis().getCaTotal()).isEqualByComparingTo("100.00");
		assertThat(result.getKpis().getMargeBrute()).isEqualByComparingTo("40.00");
		assertThat(result.getKpis().getNombreTickets()).isEqualTo(5L);
		assertThat(result.getKpis().getBonsEnAttenteMc()).isEqualTo(1);
		assertThat(result.getKpis().getAlertesStockPdv()).isEqualTo(1);
		assertThat(result.getTopProduits()).hasSize(1);
		assertThat(result.getTopProduits().getFirst().getStockRestant()).isEqualTo(12);
		assertThat(result.getBonsEnRetard()).hasSize(1);
	}

	@Test
	void synthese_invalidPeriod_returnsEmpty() {
		var result = dashboardService.synthese(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 4, 1), 1L);
		assertThat(result.getKpis().getNombreTickets()).isZero();
		assertThat(result.getCaParPdv()).isEmpty();
	}
}
