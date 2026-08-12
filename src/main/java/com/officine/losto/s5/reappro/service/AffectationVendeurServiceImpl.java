package com.officine.losto.s5.reappro.service;

import com.officine.losto.entity.AffectationVendeur;
import com.officine.losto.s5.reappro.S5Specifications;
import com.officine.losto.s5.reappro.repository.AffectationVendeurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AffectationVendeurServiceImpl {

	private final AffectationVendeurRepository repo;

	public AffectationVendeurServiceImpl(AffectationVendeurRepository repo) {
		this.repo = repo;
	}

	@Transactional(readOnly = true)
	public List<AffectationVendeur> getAll() {
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public AffectationVendeur loadById(long id) {
		return repo.findById(id).orElse(null);
	}

	@Transactional
	public AffectationVendeur save(AffectationVendeur a) {
		Objects.requireNonNull(a.getDebut());
		Objects.requireNonNull(a.getFin());
		if (a.getFin().isBefore(a.getDebut()) || a.getFin().isEqual(a.getDebut())) {
			throw new IllegalArgumentException("La fin du créneau doit être après le début");
		}
		Objects.requireNonNull(a.getPointDeVente(), "pointDeVente requis");
		Objects.requireNonNull(a.getAppUser(), "appUser requis");
		long overlaps = repo.countOverlappingSameSeller(
				a.getPointDeVente().getId(),
				a.getAppUser().getId(),
				a.getDebut(),
				a.getFin(),
				a.getId());
		if (overlaps > 0) {
			throw new IllegalArgumentException(
					"Ce vendeur a déjà une affectation qui chevauche ce créneau sur ce point de vente.");
		}
		return repo.save(a);
	}

	@Transactional
	public void remove(AffectationVendeur a) {
		repo.delete(a);
	}

	@Transactional(readOnly = true)
	public List<AffectationVendeur> findFiltered(Long pointDeVenteId, Long appUserId) {
		return repo.findAll(S5Specifications.filterAffectation(pointDeVenteId, appUserId));
	}

	/** Affectations qui intersectent la plage [from, to] (jours calendaires inclus). */
	@Transactional(readOnly = true)
	public List<AffectationVendeur> findForPlanning(Long pointDeVenteId, LocalDate from, LocalDate to) {
		Objects.requireNonNull(pointDeVenteId);
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
		if (to.isBefore(from)) {
			throw new IllegalArgumentException("La date de fin doit être après ou égale au début");
		}
		LocalDateTime rangeStartInclusive = from.atStartOfDay();
		LocalDateTime rangeEndExclusive = to.plusDays(1).atStartOfDay();
		return repo.findOverlappingRange(pointDeVenteId, rangeStartInclusive, rangeEndExclusive);
	}
}
