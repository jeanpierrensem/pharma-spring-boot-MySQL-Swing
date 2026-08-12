package com.officine.losto.commons.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Formatage monétaire unifié — Franc CFA (XOF / FCFA). */
public final class MoneyFormat {

	public static final String CODE = "XOF";
	public static final String SYMBOL = "FCFA";

	private static final DecimalFormat AMOUNT =
			new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.FRANCE));

	private MoneyFormat() {
	}

	public static String format(BigDecimal amount) {
		return formatAmount(amount) + " " + SYMBOL;
	}

	public static String formatAmount(BigDecimal amount) {
		if (amount == null) {
			return "0,00";
		}
		return AMOUNT.format(amount.setScale(2, RoundingMode.HALF_UP));
	}

	public static BigDecimal parse(String text) {
		if (text == null || text.isBlank()) {
			return null;
		}
		String s = text.trim()
				.replace(SYMBOL, "")
				.replace("CFA", "")
				.replace("€", "")
				.replace("\u00a0", "")
				.replace(" ", "")
				.replace(',', '.');
		if (s.isBlank()) {
			return null;
		}
		try {
			return new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
