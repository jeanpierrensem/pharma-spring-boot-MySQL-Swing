package com.officine.losto.s7.stocks.util;

import java.math.BigDecimal;

public final class StockPricingSupport {

	private StockPricingSupport() {
	}

	/** Marge absolue = prix vente − prix achat. */
	public static BigDecimal margin(BigDecimal costPrice, BigDecimal sellPrice) {
		if (costPrice == null || sellPrice == null) {
			return null;
		}
		return sellPrice.subtract(costPrice);
	}
}
