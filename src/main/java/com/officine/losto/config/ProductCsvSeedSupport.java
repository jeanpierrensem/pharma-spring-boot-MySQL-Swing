package com.officine.losto.config;

import com.officine.losto.entity.Form;
import com.officine.losto.entity.Packaging;
import com.officine.losto.entity.Product;
import com.officine.losto.model.FormRepo;
import com.officine.losto.model.PackagingRepo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Lecture du CSV seed produits : premières lignes comme en-têtes ; séparateur {@code ;} ;
 * accolades par nom de colonnes du fichier (<em>sans</em> sensibilité à la casse pour le mapping vers {@link Product}).
 * Les colonnes du CSV absentes dans l’entité sont ignorées. Champs absent(s) dans le CSV ou vides :
 * laissés vides ({@code null}) sur l’entité.
 */
public final class ProductCsvSeedSupport {

	static final String ENTITY_FIELD_CODE_BAR = "codeBar";
	static final String ENTITY_FIELD_NAME = "name";
	static final String ENTITY_FIELD_DOSAGE = "dosage";
	static final String ENTITY_FIELD_FORM_REF = "form";
	static final String ENTITY_FIELD_PACKAGING_REF = "packaging";
	static final String ENTITY_FIELD_FAMILLE = "famille";
	static final String ENTITY_FIELD_QUANTITY = "quantity";
	static final String ENTITY_FIELD_COST_PRICE = "costPrice";
	static final String ENTITY_FIELD_SELL_PRICE = "sellPrice";

	private ProductCsvSeedSupport() {
	}

	/** Produit catalogue + données optionnelles pour un mouvement d'entrée initial. */
	public record ProductSeedRow(
			Product product,
			Integer movementQuantity,
			BigDecimal costPrice,
			BigDecimal sellPrice) {
	}

	public static java.util.List<Product> loadProducts(Resource csvResource,
			FormRepo formRepo,
			PackagingRepo packagingRepo) throws IOException {
		return loadProductSeedRows(csvResource, formRepo, packagingRepo).stream()
				.map(ProductSeedRow::product)
				.toList();
	}

	public static java.util.List<ProductSeedRow> loadProductSeedRows(Resource csvResource,
			FormRepo formRepo,
			PackagingRepo packagingRepo) throws IOException {

		FormLookup forms = FormLookup.forRepository(formRepo);
		PackageLookup pkgs = PackageLookup.forRepository(packagingRepo);

		CSVFormat format = CSVFormat.Builder.create(CSVFormat.EXCEL)
				.setDelimiter(';')
				.setHeader()
				.setSkipHeaderRecord(true)
				.setTrim(true)
				.setIgnoreSurroundingSpaces(true)
				.setIgnoreEmptyLines(true)
				.build();

		try (BufferedReader reader = bomAwareReader(csvResource)) {
			try (CSVParser parser = CSVParser.parse(reader, format)) {
				Map<String, String> lcHeader = buildHeaderLcMap(parser.getHeaderNames());
				Map<String, ProductSeedRow> byBarcode = new java.util.LinkedHashMap<>();
				for (CSVRecord rec : parser) {
					Optional<String> codeBarOpt = text(rec, lcHeader, ENTITY_FIELD_CODE_BAR);
					if (codeBarOpt.isEmpty()) {
						continue;
					}
					String codeBar = codeBarOpt.get();
					String name = text(rec, lcHeader, ENTITY_FIELD_NAME).orElse(null);
					String dosage = text(rec, lcHeader, ENTITY_FIELD_DOSAGE).orElse(null);

					Form form = rawEntityLabel(rec, lcHeader, ENTITY_FIELD_FORM_REF)
							.flatMap(raw -> Optional.ofNullable(forms.resolve(raw)))
							.orElse(null);
					Packaging packaging = rawEntityLabel(rec, lcHeader, ENTITY_FIELD_PACKAGING_REF)
							.flatMap(raw -> Optional.ofNullable(pkgs.resolve(raw)))
							.orElse(null);

					String famille = text(rec, lcHeader, ENTITY_FIELD_FAMILLE).orElse(null);
					Integer movementQty = text(rec, lcHeader, ENTITY_FIELD_QUANTITY)
							.map(ProductCsvSeedSupport::parseIntegerEu)
							.orElse(null);
					BigDecimal costPrice = text(rec, lcHeader, ENTITY_FIELD_COST_PRICE)
							.map(ProductCsvSeedSupport::parseBigDecimalEu)
							.orElse(null);
					BigDecimal sellPrice = text(rec, lcHeader, ENTITY_FIELD_SELL_PRICE)
							.map(ProductCsvSeedSupport::parseBigDecimalEu)
							.orElse(null);
					Product p = Product.builder()
							.version(0)
							.codeBar(emptyToNull(codeBar))
							.name(emptyToNull(name))
							.dosage(emptyToNull(dosage))
							.form(form)
							.packaging(packaging)
							.famille(emptyToNull(famille))
							.thresholds(new HashSet<>())
							.build();
					byBarcode.put(codeBar.trim(), new ProductSeedRow(p, movementQty, costPrice, sellPrice));
				}
				return new java.util.ArrayList<>(byBarcode.values());
			}
		}
	}

	private static BufferedReader bomAwareReader(Resource csvResource) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8));
		br.mark(1);
		int first = br.read();
		if (first != '\ufeff') {
			br.reset();
		}
		return br;
	}

	private static Map<String, String> buildHeaderLcMap(Collection<String> headerNames) {
		Map<String, String> m = new HashMap<>();
		if (headerNames == null) {
			return m;
		}
		for (String h : headerNames) {
			if (h == null || h.isBlank()) {
				continue;
			}
			m.put(normalizeCsvHeaderKey(h), h);
		}
		return m;
	}

	static String normalizeCsvHeaderKey(String h) {
		return h.strip().replace("\ufeff", "").toLowerCase(Locale.ROOT);
	}

	static Optional<String> text(CSVRecord rec, Map<String, String> lcHeader, String entityPropLc) {
		String col = lcHeader.get(entityPropLc);
		if (col == null) {
			col = lcHeader.get(entityPropLc.toLowerCase(Locale.ROOT));
		}
		if (col == null) {
			return Optional.empty();
		}
		String v = rec.get(col);
		if (v == null) {
			return Optional.empty();
		}
		v = v.strip();
		return v.isEmpty() ? Optional.empty() : Optional.of(v);
	}

	private static Optional<String> rawEntityLabel(CSVRecord rec, Map<String, String> lcHeader, String entityPropLc) {
		return text(rec, lcHeader, entityPropLc);
	}

	static String emptyToNull(String s) {
		return s == null || s.isBlank() ? null : s.strip();
	}

	static BigDecimal parseBigDecimalEu(String raw) {
		if (raw == null || raw.isBlank()) {
			return null;
		}
		String s = raw.replace('\u00a0', ' ').strip().replace(" ", "");
		if (s.contains(",") && s.contains(".")) {
			s = s.replace(".", "").replace(",", ".");
		} else if (s.contains(",")) {
			s = s.replace(",", ".");
		}
		return new BigDecimal(s);
	}

	static Integer parseIntegerEu(String raw) {
		if (raw == null || raw.isBlank()) {
			return null;
		}
		String s = raw.replace('\u00a0', ' ').replace(" ", "").strip();
		if (s.contains(",") && s.matches(".*,[0-9]+$")) {
			s = s.replace(",", ".");
			return (int) Math.round(Double.parseDouble(s));
		}
		if (s.contains(",")) {
			s = s.replace(",", "");
		}
		return Integer.parseInt(s);
	}

	private static final class FormLookup {
		private final FormRepo repo;
		private final Map<String, Form> byKey = new HashMap<>();

		FormLookup(FormRepo repo) {
			this.repo = repo;
			for (Form f : repo.findAll()) {
				index(f.getCode(), f);
				index(f.getDescription(), f);
			}
		}

		static FormLookup forRepository(FormRepo repo) {
			return new FormLookup(repo);
		}

		private void index(String label, Form f) {
			if (label == null || f == null || label.isBlank()) {
				return;
			}
			byKey.putIfAbsent(label.strip().toLowerCase(Locale.ROOT), f);
		}

		Form resolve(String raw) {
			String trimmed = raw.strip();
			if (trimmed.isEmpty()) {
				return null;
			}
			String key = trimmed.toLowerCase(Locale.ROOT);
			Form existing = byKey.get(key);
			if (existing != null) {
				return existing;
			}
			Form byCode = repo.findByCode(trimmed);
			if (byCode != null) {
				index(byCode.getCode(), byCode);
				index(byCode.getDescription(), byCode);
				return byCode;
			}
			String codeBase = asciiCodeBase(trimmed);
			String uniqueCode = uniqueFormCode(repo, codeBase);
			Form created = repo.save(Form.builder().code(uniqueCode).description(trimmed).build());
			index(created.getCode(), created);
			index(created.getDescription(), created);
			byKey.put(key, created);
			return created;
		}

		private static String uniqueFormCode(FormRepo repo, String base) {
			String root = base.length() > 252 ? base.substring(0, 252) : base;
			int seq = 0;
			String candidate = root.substring(0, Math.min(root.length(), 240));
			while (seq < 10_000 && repo.findByCode(candidate) != null) {
				seq++;
				String suffix = "-" + seq;
				candidate = root.substring(0, Math.min(root.length(), 255 - suffix.length())) + suffix;
			}
			return candidate.length() > 255 ? candidate.substring(0, 255) : candidate;
		}
	}

	private static final class PackageLookup {
		private final PackagingRepo repo;
		private final Map<String, Packaging> byKey = new HashMap<>();

		PackageLookup(PackagingRepo repo) {
			this.repo = repo;
			for (Packaging p : repo.findAll()) {
				index(p.getCode(), p);
				if (p.getDescription() != null && !p.getDescription().isBlank()) {
					index(p.getDescription(), p);
				}
			}
		}

		static PackageLookup forRepository(PackagingRepo repo) {
			return new PackageLookup(repo);
		}

		private void index(String label, Packaging p) {
			if (label == null || p == null || label.isBlank()) {
				return;
			}
			byKey.putIfAbsent(label.strip().toLowerCase(Locale.ROOT), p);
		}

		Packaging resolve(String raw) {
			String trimmed = raw.strip();
			if (trimmed.isEmpty()) {
				return null;
			}
			String key = trimmed.toLowerCase(Locale.ROOT);
			Packaging existing = byKey.get(key);
			if (existing != null) {
				return existing;
			}
			Packaging byCode = repo.findByCode(trimmed);
			if (byCode != null) {
				index(byCode.getCode(), byCode);
				if (byCode.getDescription() != null) {
					index(byCode.getDescription(), byCode);
				}
				return byCode;
			}
			String uniqueCode = uniquePackCode(repo, trimmed);
			Packaging created = Packaging.builder().code(uniqueCode).description(trimmed).build();
			created = repo.save(created);
			index(created.getCode(), created);
			index(trimmed, created);
			return created;
		}

		private static String uniquePackCode(PackagingRepo repo, String raw) {
			String root = asciiCodeBase(raw.replace("/", "-"));
			if (root.length() > 240) {
				root = root.substring(0, 240);
			}
			String candidate = root;
			int seq = 0;
			while (seq < 10_000 && repo.findByCode(candidate) != null) {
				seq++;
				String suffix = "-" + seq;
				candidate = root.substring(0, Math.min(root.length(), 255 - suffix.length())) + suffix;
			}
			return candidate.length() > 255 ? candidate.substring(0, 255) : candidate;
		}
	}

	private static String asciiCodeBase(String input) {
		String n = Normalizer.normalize(input.strip(), Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
		n = n.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]+", "-");
		n = n.replaceAll("^-+|-+$", "").replaceAll("-{2,}", "-");
		if (n.isEmpty()) {
			return "CSV";
		}
		return n;
	}

}
