package com.officine.losto.s7.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.MouvementStock;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.entity.StockCentral;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl;
import com.officine.losto.s7.dashboard.dto.DashboardAlerteDto;
import com.officine.losto.s7.dashboard.dto.DashboardBonResumeDto;
import com.officine.losto.s7.dashboard.dto.DashboardKpisDto;
import com.officine.losto.s7.dashboard.dto.DashboardMouvementDto;
import com.officine.losto.s7.dashboard.dto.DashboardSeriePointDto;
import com.officine.losto.s7.dashboard.dto.DashboardSyntheseDto;
import com.officine.losto.s7.dashboard.dto.DashboardTopProduitDto;
import com.officine.losto.s7.dashboard.dto.DashboardVenteRecenteDto;
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

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final int TOP_PRODUITS_LIMIT = 5;
	private static final int MAX_ALERTES = 50;
	private static final int MAX_VENTES = 10;
	private static final int MAX_MOUVEMENTS = 12;
	private static final int RETARD_HEURES_SEUIL = 48;

	private final PilotageVenteService pilotageVenteService;
	private final DashboardQueryRepository dashboardQueryRepository;
	private final DashboardBonQueryRepository dashboardBonQueryRepository;
	private final BonCommandeInterneServiceImpl bonCommandeInterneService;
	private final MagasinCentralRepository magasinCentralRepository;
	private final StockPdvRepository stockPdvRepository;
	private final StockCentralRepository stockCentralRepository;
	private final MouvementStockRepository mouvementStockRepository;

	public DashboardServiceImpl(
			PilotageVenteService pilotageVenteService,
			DashboardQueryRepository dashboardQueryRepository,
			DashboardBonQueryRepository dashboardBonQueryRepository,
			BonCommandeInterneServiceImpl bonCommandeInterneService,
			MagasinCentralRepository magasinCentralRepository,
			StockPdvRepository stockPdvRepository,
			StockCentralRepository stockCentralRepository,
			MouvementStockRepository mouvementStockRepository) {
		this.pilotageVenteService = pilotageVenteService;
		this.dashboardQueryRepository = dashboardQueryRepository;
		this.dashboardBonQueryRepository = dashboardBonQueryRepository;
		this.bonCommandeInterneService = bonCommandeInterneService;
		this.magasinCentralRepository = magasinCentralRepository;
		this.stockPdvRepository = stockPdvRepository;
		this.stockCentralRepository = stockCentralRepository;
		this.mouvementStockRepository = mouvementStockRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public DashboardSyntheseDto synthese(LocalDate dtDebut, LocalDate dtFin, Long siteIdOptionnel) {
		if (dtDebut == null || dtFin == null || dtDebut.isAfter(dtFin)) {
			return emptySynthese(siteIdOptionnel, dtDebut, dtFin);
		}

		PeriodeFiltre filtre = new PeriodeFiltre(dtDebut, dtFin, siteIdOptionnel);
		List<ResultatCaParPdv> caParPdv = pilotageVenteService.caParPointDeVente(filtre);
		BigDecimal caTotal = sumCa(caParPdv);
		long nombreTickets = dashboardQueryRepository.countTickets(dtDebut, dtFin, siteIdOptionnel);

		long jours = ChronoUnit.DAYS.between(dtDebut, dtFin) + 1;
		LocalDate prevFin = dtDebut.minusDays(1);
		LocalDate prevDebut = prevFin.minusDays(jours - 1);
		BigDecimal caPrecedent = sumCa(pilotageVenteService.caParPointDeVente(
				new PeriodeFiltre(prevDebut, prevFin, siteIdOptionnel)));
		long ticketsPrecedent = dashboardQueryRepository.countTickets(prevDebut, prevFin, siteIdOptionnel);

		MargeCalc marge = computeMarge(dtDebut, dtFin, siteIdOptionnel);
		List<DashboardSeriePointDto> sparkline = mapCaParJour(
				dashboardQueryRepository.caParJour(dtDebut, dtFin, siteIdOptionnel));

		List<ClassementProduit> classement =
				pilotageVenteService.topProduitsParCa(filtre, TOP_PRODUITS_LIMIT, PilotageProduitSort.CA);
		List<DashboardTopProduitDto> topProduits = enrichTopProduits(classement, siteIdOptionnel);

		Optional<MagasinCentral> magasinOpt = resolveMagasin(siteIdOptionnel);
		List<DashboardBonResumeDto> bonsEnCours = magasinOpt
				.map(mc -> bonCommandeInterneService.findEnCoursTraitementMagasin(mc.getId()).stream()
						.map(this::toBonResume)
						.sorted(Comparator.comparing(DashboardBonResumeDto::getOrderDate,
								Comparator.nullsLast(Comparator.reverseOrder())))
						.toList())
				.orElseGet(List::of);
		List<DashboardBonResumeDto> bonsEnRetard = bonsEnCours.stream().filter(DashboardBonResumeDto::isEnRetard).toList();

		List<StockPdv> stocksPdv = stockPdvRepository.findForDashboard(siteIdOptionnel);
		List<StockCentral> stocksCentral = siteIdOptionnel != null
				? stockCentralRepository.findBySite_Id(siteIdOptionnel)
				: stockCentralRepository.findAll();

		List<DashboardAlerteDto> alertes = new ArrayList<>();
		List<DashboardAlerteDto> ruptures = new ArrayList<>();
		int alertesPdv = collectStockPdvAlertes(stocksPdv, alertes, ruptures);
		int alertesCentral = collectStockCentralAlertes(stocksCentral, alertes);
		alertes.sort(Comparator
				.comparing(DashboardAlerteDto::getSeverite, severiteOrder())
				.thenComparing(DashboardAlerteDto::getLibelle));
		if (alertes.size() > MAX_ALERTES) {
			alertes = new ArrayList<>(alertes.subList(0, MAX_ALERTES));
		}

		List<DashboardVenteRecenteDto> dernieresVentes = mapDernieresVentes(
				dashboardQueryRepository.dernieresVentes(
						dtDebut, dtFin, siteIdOptionnel, PageRequest.of(0, MAX_VENTES)));

		List<DashboardMouvementDto> mouvements = mapMouvements(siteIdOptionnel);

		List<DashboardSeriePointDto> bonsTraitesParJour = fillDailySeries(
				dtDebut,
				dtFin,
				mapBonsTraitesParJour(
						dashboardBonQueryRepository.bonsParJour(
								dtDebut, dtFin, siteIdOptionnel, StatutBonCommandeInterne.TRAITE)));

		return DashboardSyntheseDto.builder()
				.siteId(siteIdOptionnel)
				.magasinCentralId(magasinOpt.map(MagasinCentral::getId).orElse(null))
				.magasinCentralLibelle(magasinOpt.map(MagasinCentral::getLibelle).orElse(null))
				.dtDebut(dtDebut)
				.dtFin(dtFin)
				.kpis(DashboardKpisDto.builder()
						.caTotal(caTotal)
						.nombreTickets(nombreTickets)
						.margeBrute(marge.marge())
						.tauxMargePct(marge.tauxPct())
						.bonsEnAttenteMc(bonsEnCours.size())
						.alertesStockPdv(alertesPdv)
						.alertesStockCentral(alertesCentral)
						.evolutionCaPct(evolutionPct(caTotal, caPrecedent))
						.evolutionTicketsPct(evolutionPct(
								BigDecimal.valueOf(nombreTickets), BigDecimal.valueOf(ticketsPrecedent)))
						.sparklineCa(sparkline)
						.build())
				.caParPdv(caParPdv)
				.topProduits(topProduits)
				.bonsEnCours(bonsEnCours)
				.alertes(alertes)
				.dernieresVentes(dernieresVentes)
				.mouvementsRecents(mouvements)
				.bonsTraitesParJour(bonsTraitesParJour)
				.rupturesPdv(ruptures)
				.bonsEnRetard(bonsEnRetard)
				.build();
	}

	private List<DashboardTopProduitDto> enrichTopProduits(List<ClassementProduit> classement, Long siteId) {
		List<DashboardTopProduitDto> out = new ArrayList<>(classement.size());
		for (ClassementProduit c : classement) {
			Integer stock = c.getProductId() == null
					? null
					: stockPdvRepository.sumQteDisponibleByProductAndSite(c.getProductId(), siteId);
			out.add(DashboardTopProduitDto.builder()
					.rang(c.getRang())
					.productId(c.getProductId())
					.libelle(c.getLibelle())
					.chiffreAffaires(c.getChiffreAffaires())
					.quantiteVendue(c.getQuantiteVendue())
					.stockRestant(stock)
					.build());
		}
		return out;
	}

	private MargeCalc computeMarge(LocalDate from, LocalDate to, Long siteId) {
		List<Object[]> rows = dashboardQueryRepository.caEtCout(from, to, siteId);
		if (rows.isEmpty() || rows.getFirst() == null) {
			return new MargeCalc(BigDecimal.ZERO, BigDecimal.ZERO);
		}
		Object[] r = rows.getFirst();
		BigDecimal ca = toBd(r[0]);
		BigDecimal cout = toBd(r[1]);
		BigDecimal marge = ca.subtract(cout);
		BigDecimal taux = BigDecimal.ZERO;
		if (ca.compareTo(BigDecimal.ZERO) > 0) {
			taux = marge.multiply(BigDecimal.valueOf(100)).divide(ca, 2, RoundingMode.HALF_UP);
		}
		return new MargeCalc(marge, taux);
	}

	private List<DashboardMouvementDto> mapMouvements(Long siteId) {
		List<MouvementStock> rows = siteId != null
				? mouvementStockRepository.findBySite_IdOrderByDateMouvementDesc(siteId)
				: mouvementStockRepository.findAll();
		return rows.stream()
				.sorted(Comparator.comparing(
						MouvementStock::getDateMouvement, Comparator.nullsLast(Comparator.reverseOrder())))
				.limit(MAX_MOUVEMENTS)
				.map(m -> DashboardMouvementDto.builder()
						.id(m.getId())
						.typeMouvement(m.getTypeMouvement())
						.productName(m.getProduct() != null ? m.getProduct().getName() : "?")
						.quantiteAlgebrique(m.getQuantiteAlgebrique())
						.dateMouvement(m.getDateMouvement())
						.commentaire(m.getCommentaire())
						.build())
				.toList();
	}

	private static List<DashboardSeriePointDto> mapCaParJour(List<Object[]> rows) {
		List<DashboardSeriePointDto> out = new ArrayList<>(rows.size());
		for (Object[] r : rows) {
			out.add(DashboardSeriePointDto.builder()
					.date((LocalDate) r[0])
					.valeur(toBd(r[1]))
					.build());
		}
		return out;
	}

	private static List<DashboardSeriePointDto> mapBonsTraitesParJour(List<Object[]> rows) {
		List<DashboardSeriePointDto> out = new ArrayList<>(rows.size());
		for (Object[] r : rows) {
			out.add(DashboardSeriePointDto.builder()
					.date((LocalDate) r[0])
					.valeur(BigDecimal.valueOf(toLong(r[1])))
					.build());
		}
		return out;
	}

	/** Complète la série avec un point par jour (0 si aucun bon traité ce jour-là). */
	private static List<DashboardSeriePointDto> fillDailySeries(
			LocalDate from, LocalDate to, List<DashboardSeriePointDto> points) {
		Map<LocalDate, BigDecimal> byDate = new HashMap<>();
		if (points != null) {
			for (DashboardSeriePointDto p : points) {
				if (p.getDate() != null && p.getValeur() != null) {
					byDate.put(p.getDate(), p.getValeur());
				}
			}
		}
		List<DashboardSeriePointDto> out = new ArrayList<>();
		for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
			out.add(DashboardSeriePointDto.builder()
					.date(d)
					.valeur(byDate.getOrDefault(d, BigDecimal.ZERO))
					.build());
		}
		return out;
	}

	private static List<DashboardVenteRecenteDto> mapDernieresVentes(List<Object[]> rows) {
		List<DashboardVenteRecenteDto> out = new ArrayList<>(rows.size());
		for (Object[] r : rows) {
			out.add(DashboardVenteRecenteDto.builder()
					.id((Long) r[0])
					.number(r[1] != null ? r[1].toString() : null)
					.client(r[2] != null ? r[2].toString() : null)
					.totalPrice(toBd(r[3]))
					.dateVente((LocalDate) r[4])
					.pointDeVenteLibelle(r[5] != null ? r[5].toString() : null)
					.paymentStatus(r[6] != null ? r[6].toString() : null)
					.build());
		}
		return out;
	}

	private DashboardBonResumeDto toBonResume(BonCommandeInterne bon) {
		PointDeVente pdv = bon.getPointDeVente();
		long heures = 0;
		boolean enRetard = false;
		if (bon.getOrderDate() != null) {
			LocalDateTime debut = bon.getOrderDate().atStartOfDay();
			heures = ChronoUnit.HOURS.between(debut, LocalDateTime.now());
			enRetard = heures >= RETARD_HEURES_SEUIL
					&& (bon.getStatut() == StatutBonCommandeInterne.ENVOYE
							|| bon.getStatut() == StatutBonCommandeInterne.PARTIEL);
		}
		return DashboardBonResumeDto.builder()
				.id(bon.getId())
				.number(bon.getNumber())
				.orderDate(bon.getOrderDate())
				.statut(bon.getStatut())
				.statutLibelle(bon.getStatut() != null ? bon.getStatut().getLibelle() : null)
				.pointDeVenteId(pdv != null ? pdv.getId() : null)
				.pointDeVenteLibelle(pdv != null ? pdv.getLibelle() : null)
				.enRetard(enRetard)
				.heuresEnAttente(heures)
				.priorite(prioriteBon(heures, enRetard))
				.build();
	}

	private static String prioriteBon(long heures, boolean enRetard) {
		if (!enRetard) {
			return "BASSE";
		}
		if (heures >= 96) {
			return "HAUTE";
		}
		return "MOYENNE";
	}

	private static int collectStockPdvAlertes(
			List<StockPdv> stocks, List<DashboardAlerteDto> alertes, List<DashboardAlerteDto> ruptures) {
		int count = 0;
		for (StockPdv sp : stocks) {
			Integer qte = sp.getQteDisponible();
			Integer seuil = sp.getQteSeuilAlerte();
			if (qte != null && qte == 0) {
				DashboardAlerteDto rupture = stockPdvAlerte(sp, "RUPTURE_PDV", "CRITIQUE");
				ruptures.add(rupture);
				alertes.add(rupture);
				count++;
				continue;
			}
			if (!isBelowAlert(qte, seuil)) {
				continue;
			}
			count++;
			alertes.add(stockPdvAlerte(sp, "STOCK_PDV_BAS", "AVERTISSEMENT"));
		}
		return count;
	}

	private static DashboardAlerteDto stockPdvAlerte(StockPdv sp, String type, String severite) {
		PointDeVente pdv = sp.getPointDeVente();
		String pdvLib = pdv != null ? pdv.getLibelle() : "";
		String prodName = sp.getProduct() != null ? sp.getProduct().getName() : "?";
		return DashboardAlerteDto.builder()
				.type(type)
				.severite(severite)
				.libelle(prodName)
				.detail(pdvLib + " — qte " + sp.getQteDisponible() + " / seuil " + sp.getQteSeuilAlerte())
				.entityId(sp.getId())
				.build();
	}

	private static int collectStockCentralAlertes(List<StockCentral> stocks, List<DashboardAlerteDto> out) {
		int count = 0;
		for (StockCentral sc : stocks) {
			if (!isBelowAlert(sc.getQteDisponible(), sc.getQteSeuilAlerte())) {
				continue;
			}
			count++;
			String prodName = sc.getProduct() != null ? sc.getProduct().getName() : "?";
			String batch = sc.getBatch() != null ? sc.getBatch().getNumber() : "";
			String detail = "qte " + sc.getQteDisponible() + " / seuil " + sc.getQteSeuilAlerte();
			if (!batch.isBlank()) {
				detail = "lot " + batch + " — " + detail;
			}
			String severite = sc.getQteDisponible() != null && sc.getQteDisponible() == 0 ? "CRITIQUE" : "AVERTISSEMENT";
			out.add(DashboardAlerteDto.builder()
					.type("STOCK_CENTRAL_BAS")
					.severite(severite)
					.libelle(prodName)
					.detail(detail)
					.entityId(sc.getId())
					.build());
		}
		return count;
	}

	private static Comparator<String> severiteOrder() {
		return Comparator.comparingInt(s -> switch (s == null ? "" : s) {
			case "CRITIQUE" -> 0;
			case "AVERTISSEMENT" -> 1;
			default -> 2;
		});
	}

	private static BigDecimal sumCa(List<ResultatCaParPdv> rows) {
		return rows.stream()
				.map(ResultatCaParPdv::getChiffreAffaires)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private static BigDecimal evolutionPct(BigDecimal current, BigDecimal previous) {
		if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
			return current != null && current.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
		}
		if (current == null) {
			return BigDecimal.valueOf(-100);
		}
		return current.subtract(previous)
				.multiply(BigDecimal.valueOf(100))
				.divide(previous, 1, RoundingMode.HALF_UP);
	}

	private Optional<MagasinCentral> resolveMagasin(Long siteIdOptionnel) {
		if (siteIdOptionnel == null) {
			return Optional.empty();
		}
		return magasinCentralRepository.findBySite_Id(siteIdOptionnel);
	}

	private static boolean isBelowAlert(Integer qteDisponible, Integer qteSeuilAlerte) {
		return qteDisponible != null && qteSeuilAlerte != null && qteDisponible < qteSeuilAlerte;
	}

	private static DashboardSyntheseDto emptySynthese(Long siteId, LocalDate from, LocalDate to) {
		return DashboardSyntheseDto.builder()
				.siteId(siteId)
				.dtDebut(from)
				.dtFin(to)
				.kpis(emptyKpis())
				.caParPdv(List.of())
				.topProduits(List.of())
				.bonsEnCours(List.of())
				.alertes(List.of())
				.dernieresVentes(List.of())
				.mouvementsRecents(List.of())
				.bonsTraitesParJour(List.of())
				.rupturesPdv(List.of())
				.bonsEnRetard(List.of())
				.build();
	}

	private static DashboardKpisDto emptyKpis() {
		return DashboardKpisDto.builder()
				.caTotal(BigDecimal.ZERO)
				.nombreTickets(0L)
				.margeBrute(BigDecimal.ZERO)
				.tauxMargePct(BigDecimal.ZERO)
				.bonsEnAttenteMc(0)
				.alertesStockPdv(0)
				.alertesStockCentral(0)
				.evolutionCaPct(BigDecimal.ZERO)
				.evolutionTicketsPct(BigDecimal.ZERO)
				.sparklineCa(List.of())
				.build();
	}

	private static BigDecimal toBd(Object o) {
		if (o == null) {
			return BigDecimal.ZERO;
		}
		if (o instanceof BigDecimal bd) {
			return bd;
		}
		if (o instanceof Number n) {
			return BigDecimal.valueOf(n.doubleValue());
		}
		return new BigDecimal(o.toString());
	}

	private static long toLong(Object o) {
		if (o == null) {
			return 0L;
		}
		if (o instanceof Number n) {
			return n.longValue();
		}
		return Long.parseLong(o.toString());
	}

	private record MargeCalc(BigDecimal marge, BigDecimal tauxPct) {
	}
}
