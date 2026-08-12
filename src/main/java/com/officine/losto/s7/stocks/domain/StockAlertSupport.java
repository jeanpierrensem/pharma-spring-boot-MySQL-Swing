package com.officine.losto.s7.stocks.domain;

/**
 * Règle d'alerte stock : disponible strictement inférieur au seuil alerte
 * (alignée sur la coloration des lignes stock PDV / central).
 */
public final class StockAlertSupport {

	public static final String STOCK_BAS_CODE = "THR-STOCK-BAS";

	private StockAlertSupport() {
	}

	public static boolean isBelowAlert(Integer qteDisponible, Integer qteSeuilAlerte) {
		return qteDisponible != null && qteSeuilAlerte != null && qteDisponible < qteSeuilAlerte;
	}

	/** Seuil effectif : valeur ligne, sinon niveau du seuil référentiel « stock bas ». */
	public static Integer effectiveSeuilAlerte(Integer rowSeuil, Integer defaultStockBasLevel) {
		if (rowSeuil != null) {
			return rowSeuil;
		}
		return defaultStockBasLevel;
	}

	/** Quantité à commander par défaut : max(0, seuil − disponible). */
	public static int defaultOrderQuantity(int qteDisponible, Integer rowSeuil, Integer defaultStockBasLevel) {
		Integer seuil = effectiveSeuilAlerte(rowSeuil, defaultStockBasLevel);
		if (seuil == null) {
			return 0;
		}
		int gap = seuil - qteDisponible;
		return Math.max(0, Math.min(gap, 999_999));
	}

	/** @deprecated utiliser {@link #defaultOrderQuantity(int, Integer, Integer)} */
	@Deprecated
	public static int suggestedReorderQuantity(int qteDisponible, int qteSeuilAlerte) {
		return defaultOrderQuantity(qteDisponible, qteSeuilAlerte, null);
	}

	public static int nz(Integer v) {
		return v == null ? 0 : v;
	}
}
