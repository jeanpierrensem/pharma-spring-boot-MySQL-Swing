package com.officine.losto.s7.pilotage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.officine.losto.s7.pilotage.dto.ClassementProduit;
import com.officine.losto.s7.pilotage.dto.PeriodeFiltre;
import com.officine.losto.s7.pilotage.dto.ResultatAnneePic;
import com.officine.losto.s7.pilotage.dto.ResultatCaParPdv;
import com.officine.losto.s7.pilotage.dto.ResultatMargeProduitLot;
import com.officine.losto.s7.pilotage.dto.ResultatMoisPic;
import com.officine.losto.s7.pilotage.repository.PilotageVenteQueryRepository;

@Service
public class PilotageVenteServiceImpl implements PilotageVenteService {

	private final PilotageVenteQueryRepository pilotageVenteQueryRepository;

	public PilotageVenteServiceImpl(PilotageVenteQueryRepository pilotageVenteQueryRepository) {
		this.pilotageVenteQueryRepository = pilotageVenteQueryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ResultatCaParPdv> caParPointDeVente(PeriodeFiltre filtre) {
		if (!periodeValide(filtre)) {
			return List.of();
		}
		List<Object[]> rows = pilotageVenteQueryRepository.caParPointDeVente(filtre.dtDebut(), filtre.dtFin(),
				filtre.siteIdOptionnel());
		List<ResultatCaParPdv> out = new ArrayList<>(rows.size());
		for (Object[] r : rows) {
			out.add(ResultatCaParPdv.builder()
					.pointDeVenteId((Long) r[0])
					.libellePdv(r[1] != null ? Objects.toString(r[1], "") : "")
					.chiffreAffaires(toBd(r[2]))
					.build());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ResultatMargeProduitLot> margesParProduitEtLot(PeriodeFiltre filtre) {
		if (!periodeValide(filtre)) {
			return List.of();
		}
		List<Object[]> rows = pilotageVenteQueryRepository.margesParProduitEtLot(filtre.dtDebut(), filtre.dtFin(),
				filtre.siteIdOptionnel());
		List<ResultatMargeProduitLot> out = new ArrayList<>(rows.size());
		for (Object[] r : rows) {
			BigDecimal ca = toBd(r[4]);
			BigDecimal cout = toBd(r[5]);
			BigDecimal marge = ca.subtract(cout);
			BigDecimal taux = BigDecimal.ZERO;
			if (ca.compareTo(BigDecimal.ZERO) > 0) {
				taux = marge.divide(ca, 6, RoundingMode.HALF_UP);
			}
			long qty = toLong(r[3]);
			out.add(ResultatMargeProduitLot.builder()
					.pointDeVenteId((Long) r[0])
					.productId((Long) r[1])
					.batchId((Long) r[2])
					.quantiteVendue(qty)
					.chiffreAffaires(ca)
					.coutRevient(cout)
					.marge(marge)
					.tauxMarge(taux)
					.build());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClassementProduit> topProduitsParCa(PeriodeFiltre filtre, int limit, PilotageProduitSort sort) {
		if (!periodeValide(filtre) || limit <= 0) {
			return List.of();
		}
		List<Object[]> rows = pilotageVenteQueryRepository.aggregateProduits(filtre.dtDebut(), filtre.dtFin(),
				filtre.siteIdOptionnel());
		Comparator<Object[]> cmp = sort == PilotageProduitSort.QUANTITE
				? Comparator.comparingLong((Object[] a) -> toLong(a[3])).reversed()
						.thenComparing((Object[] a) -> toBd(a[2]), Comparator.reverseOrder())
				: Comparator.comparing((Object[] a) -> toBd(a[2]), Comparator.reverseOrder())
						.thenComparingLong((Object[] a) -> toLong(a[3])).reversed();
		rows.sort(cmp);
		List<ClassementProduit> out = new ArrayList<>(Math.min(limit, rows.size()));
		int rang = 1;
		for (Object[] r : rows) {
			if (rang > limit) {
				break;
			}
			out.add(ClassementProduit.builder()
					.rang(rang++)
					.productId((Long) r[0])
					.libelle(r[1] != null ? Objects.toString(r[1], "") : "")
					.chiffreAffaires(toBd(r[2]))
					.quantiteVendue(toLong(r[3]))
					.build());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ResultatMoisPic> moisLesPlusActifs(int annee, Long siteIdOptionnel) {
		LocalDate start = LocalDate.of(annee, 1, 1);
		LocalDate end = LocalDate.of(annee, 12, 31);
		List<Object[]> rows = pilotageVenteQueryRepository.moisLesPlusActifs(start, end, siteIdOptionnel);
		List<ResultatMoisPic> out = new ArrayList<>(rows.size());
		int rang = 1;
		for (Object[] r : rows) {
			int mois = toInt(r[0]);
			out.add(ResultatMoisPic.builder()
					.annee(annee)
					.mois(mois)
					.chiffreAffaires(toBd(r[1]))
					.rang(rang++)
					.build());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ResultatAnneePic> anneesLesPlusActives(PeriodeFiltre filtre) {
		if (!periodeValide(filtre)) {
			return List.of();
		}
		List<Object[]> rows = pilotageVenteQueryRepository.anneesLesPlusActives(filtre.dtDebut(), filtre.dtFin(),
				filtre.siteIdOptionnel());
		List<ResultatAnneePic> out = new ArrayList<>(rows.size());
		int rang = 1;
		for (Object[] r : rows) {
			out.add(ResultatAnneePic.builder()
					.annee(toInt(r[0]))
					.chiffreAffaires(toBd(r[1]))
					.rang(rang++)
					.build());
		}
		return out;
	}

	private static boolean periodeValide(PeriodeFiltre f) {
		if (f == null || f.dtDebut() == null || f.dtFin() == null) {
			return false;
		}
		return !f.dtDebut().isAfter(f.dtFin());
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

	private static int toInt(Object o) {
		if (o == null) {
			return 0;
		}
		if (o instanceof Number n) {
			return n.intValue();
		}
		return Integer.parseInt(o.toString());
	}
}
