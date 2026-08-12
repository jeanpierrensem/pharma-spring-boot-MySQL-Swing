package com.officine.losto.s5.reappro;

import com.officine.losto.entity.AffectationVendeur;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.StatutBonCommandeInterne;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class S5Specifications {

	private S5Specifications() {
	}

	public static Specification<BonCommandeInterne> filterBon(
			Long siteId,
			Long pointDeVenteId,
			Long magasinCentralId,
			StatutBonCommandeInterne statut,
			List<StatutBonCommandeInterne> statuts,
			LocalDate from,
			LocalDate to) {
		return (root, query, cb) -> {
			List<Predicate> preds = new ArrayList<>();
			if (siteId != null) {
				Join<Object, Object> site = root.join("site", JoinType.INNER);
				preds.add(cb.equal(site.get("id"), siteId));
			}
			if (pointDeVenteId != null) {
				Join<Object, Object> pdv = root.join("pointDeVente", JoinType.INNER);
				preds.add(cb.equal(pdv.get("id"), pointDeVenteId));
			}
			if (magasinCentralId != null) {
				Join<Object, Object> mag = root.join("magasinCentral", JoinType.INNER);
				preds.add(cb.equal(mag.get("id"), magasinCentralId));
			}
			if (statuts != null && !statuts.isEmpty()) {
				preds.add(root.get("statut").in(statuts));
			} else if (statut != null) {
				preds.add(cb.equal(root.get("statut"), statut));
			}
			if (from != null) {
				preds.add(cb.greaterThanOrEqualTo(root.get("orderDate"), from));
			}
			if (to != null) {
				preds.add(cb.lessThanOrEqualTo(root.get("orderDate"), to));
			}
			if (preds.isEmpty()) {
				return cb.conjunction();
			}
			return cb.and(preds.toArray(new Predicate[0]));
		};
	}

	public static Specification<AffectationVendeur> filterAffectation(Long pointDeVenteId, Long appUserId) {
		return (root, query, cb) -> {
			List<Predicate> preds = new ArrayList<>();
			if (pointDeVenteId != null) {
				Join<Object, Object> pdv = root.join("pointDeVente", JoinType.INNER);
				preds.add(cb.equal(pdv.get("id"), pointDeVenteId));
			}
			if (appUserId != null) {
				Join<Object, Object> u = root.join("appUser", JoinType.INNER);
				preds.add(cb.equal(u.get("id"), appUserId));
			}
			if (preds.isEmpty()) {
				return cb.conjunction();
			}
			return cb.and(preds.toArray(new Predicate[0]));
		};
	}
}
