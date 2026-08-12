package com.officine.losto.s4.ventes;

import com.officine.losto.entity.Sell;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Filtres de recherche des ventes (périmètre site, PDV, période, vendeur) et texte. */
public final class SellSpecification {

	private SellSpecification() {
	}

	public static Specification<Sell> filter(
			Long siteId, Long pointDeVenteId, LocalDate from, LocalDate to, Long effectueeParUserId) {
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
			if (from != null) {
				preds.add(cb.greaterThanOrEqualTo(root.get("dateVente"), from));
			}
			if (to != null) {
				preds.add(cb.lessThanOrEqualTo(root.get("dateVente"), to));
			}
			if (effectueeParUserId != null) {
				Join<Object, Object> u = root.join("effectueePar", JoinType.INNER);
				preds.add(cb.equal(u.get("id"), effectueeParUserId));
			}
			if (preds.isEmpty()) {
				return cb.conjunction();
			}
			return cb.and(preds.toArray(new Predicate[0]));
		};
	}

	public static Specification<Sell> searchText(String number, String seller, String client, String sellType) {
		return (root, query, cb) -> {
			List<Predicate> preds = new ArrayList<>();
			if (notBlank(number)) {
				preds.add(cb.like(cb.lower(root.get("number")), "%" + number.trim().toLowerCase() + "%"));
			}
			if (notBlank(seller)) {
				preds.add(cb.like(cb.lower(root.get("seller")), "%" + seller.trim().toLowerCase() + "%"));
			}
			if (notBlank(client)) {
				preds.add(cb.like(cb.lower(root.get("client")), "%" + client.trim().toLowerCase() + "%"));
			}
			if (notBlank(sellType)) {
				preds.add(cb.like(cb.lower(root.get("sellType")), "%" + sellType.trim().toLowerCase() + "%"));
			}
			if (preds.isEmpty()) {
				return cb.conjunction();
			}
			return cb.and(preds.toArray(new Predicate[0]));
		};
	}

	private static boolean notBlank(String s) {
		return s != null && !s.isBlank();
	}
}
