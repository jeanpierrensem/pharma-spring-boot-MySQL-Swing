package com.officine.losto.s5.reappro.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.officine.losto.dto.EntityRefDto;
import com.officine.losto.dto.mapper.DtoMapper;
import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.exception.RestExceptionHandler;
import com.officine.losto.s5.reappro.dto.BonCommandeInterneMergeResult;
import com.officine.losto.s5.reappro.dto.BonCommandeInterneRequestDto;
import com.officine.losto.s5.reappro.dto.BonCommandeInterneResponseDto;
import com.officine.losto.s5.reappro.dto.LigneBonCommandeInterneRequestDto;
import com.officine.losto.s5.reappro.dto.LigneBonCommandeInterneResponseDto;
import com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BonCommandeInterneController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
@ActiveProfiles("test")
class BonCommandeInterneControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private BonCommandeInterneServiceImpl bonService;

	@MockitoBean
	private DtoMapper dtoMapper;

	@Test
	void getAll_returnsEmptyJsonArray() throws Exception {
		when(bonService.getAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/bons-commande-interne"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
		verify(bonService).getAll();
		verify(dtoMapper, never()).toBonResponse(any(BonCommandeInterne.class));
	}

	@Test
	void getAll_returnsMappedItems() throws Exception {
		BonCommandeInterne b = bonEntity(10L, StatutBonCommandeInterne.ENVOYE);
		when(bonService.getAll()).thenReturn(List.of(b));
		when(dtoMapper.toBonResponse(b))
				.thenReturn(sampleResponseDto(10L, StatutBonCommandeInterne.ENVOYE));

		mockMvc.perform(get("/api/bons-commande-interne"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(10))
				.andExpect(jsonPath("$[0].statut").value("ENVOYE"))
				.andExpect(jsonPath("$[0].statutLibelle").value("Envoyé"));

		verify(dtoMapper).toBonResponse(b);
	}

	@Test
	void getById_returnsMappedDto() throws Exception {
		BonCommandeInterne b = bonEntity(7L, StatutBonCommandeInterne.BROUILLON);
		when(bonService.loadById(7L)).thenReturn(b);
		when(dtoMapper.toBonResponse(b)).thenReturn(sampleResponseDto(7L, StatutBonCommandeInterne.BROUILLON));

		mockMvc.perform(get("/api/bons-commande-interne/7"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.statut").value("BROUILLON"))
				.andExpect(jsonPath("$.statutLibelle").value("Brouillon"));

		verify(bonService).loadById(7L);
	}

	@Test
	void getById_whenNotFound_returnsOkWithEmptyBody() throws Exception {
		when(bonService.loadById(404L)).thenReturn(null);
		when(dtoMapper.toBonResponse(isNull())).thenReturn(null);

		mockMvc.perform(get("/api/bons-commande-interne/404"))
				.andExpect(status().isOk())
				.andExpect(content().string(""));

		verify(dtoMapper).toBonResponse(isNull());
	}

	@Test
	void getById_invalidPath_returns400() throws Exception {
		mockMvc.perform(get("/api/bons-commande-interne/not-a-number")).andExpect(status().isBadRequest());
		verify(bonService, never()).loadById(anyLong());
	}

	@Test
	void filter_passesParamsAndReturnsMappedList() throws Exception {
		when(bonService.findFiltered(
						eq(1L),
						eq(2L),
						eq(44L),
						eq(StatutBonCommandeInterne.ENVOYE),
						isNull(),
						eq(LocalDate.of(2026, 4, 1)),
						eq(LocalDate.of(2026, 4, 30))))
				.thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/bons-commande-interne/filter")
						.param("siteId", "1")
						.param("pointDeVenteId", "2")
						.param("magasinCentralId", "44")
						.param("statut", "ENVOYE")
						.param("from", "2026-04-01")
						.param("to", "2026-04-30"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));

		verify(bonService).findFiltered(
				eq(1L),
				eq(2L),
				eq(44L),
				eq(StatutBonCommandeInterne.ENVOYE),
				isNull(),
				eq(LocalDate.of(2026, 4, 1)),
				eq(LocalDate.of(2026, 4, 30)));
	}

	@Test
	void filter_withStatutsList_passesListToService() throws Exception {
		when(bonService.findFiltered(
						isNull(),
						isNull(),
						eq(44L),
						isNull(),
						eq(List.of(StatutBonCommandeInterne.ENVOYE, StatutBonCommandeInterne.PARTIEL)),
						isNull(),
						isNull()))
				.thenReturn(Collections.emptyList());

		mockMvc.perform(
						get("/api/bons-commande-interne/filter")
								.param("magasinCentralId", "44")
								.param("statuts", "ENVOYE", "PARTIEL"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));

		verify(bonService)
				.findFiltered(
						isNull(),
						isNull(),
						eq(44L),
						isNull(),
						eq(List.of(StatutBonCommandeInterne.ENVOYE, StatutBonCommandeInterne.PARTIEL)),
						isNull(),
						isNull());
	}

	@Test
	void enCoursTraitement_returnsEnvoyeAndPartiel() throws Exception {
		BonCommandeInterne partiel = bonEntity(8L, StatutBonCommandeInterne.PARTIEL);
		when(bonService.findEnCoursTraitementMagasin(44L)).thenReturn(List.of(partiel));
		when(dtoMapper.toBonResponse(partiel))
				.thenReturn(sampleResponseDto(8L, StatutBonCommandeInterne.PARTIEL));

		mockMvc.perform(get("/api/bons-commande-interne/en-cours-traitement").param("magasinCentralId", "44"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(8))
				.andExpect(jsonPath("$[0].statut").value("PARTIEL"))
				.andExpect(jsonPath("$[0].statutLibelle").value("Partiel"));

		verify(bonService).findEnCoursTraitementMagasin(44L);
	}

	@Test
	void filter_invalidDateParam_returns400() throws Exception {
		mockMvc.perform(get("/api/bons-commande-interne/filter").param("from", "bad-date"))
				.andExpect(status().isBadRequest());

		verify(bonService, never())
				.findFiltered(any(), any(), any(), any(), any(), any(), any());
	}

	@Test
	void create_withoutRequiredFields_returns400() throws Exception {
		mockMvc.perform(post("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors").isArray());

		verify(dtoMapper, never()).toBon(any());
		verify(bonService, never()).save(any(BonCommandeInterne.class));
	}

	@Test
	void create_validDraftWithoutLines_returns200AndChainsMapperAndSave() throws Exception {
		BonCommandeInterneRequestDto req = BonCommandeInterneRequestDto.builder()
				.orderDate(LocalDate.of(2026, 5, 10))
				.pointDeVenteId(11L)
				.siteId(12L)
				.userId(13L)
				.magasinCentralId(14L)
				.number("BINT-NEW")
				.lines(null)
				.build();

		BonCommandeInterne mapped = BonCommandeInterne.builder()
				.number("BINT-NEW")
				.orderDate(LocalDate.of(2026, 5, 10))
				.lignes(new ArrayList<>())
				.build();
		BonCommandeInterne saved = bonEntity(100L, StatutBonCommandeInterne.BROUILLON);
		saved.setNumber("BINT-NEW");

		when(dtoMapper.toBon(any(BonCommandeInterneRequestDto.class))).thenReturn(mapped);
		when(bonService.save(mapped)).thenReturn(saved);
		when(dtoMapper.toBonResponse(saved)).thenReturn(sampleResponseDto(100L, StatutBonCommandeInterne.BROUILLON));

		mockMvc.perform(post("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(100))
				.andExpect(jsonPath("$.statut").value("BROUILLON"));

		verify(dtoMapper).toBon(any(BonCommandeInterneRequestDto.class));
		verify(bonService).save(mapped);
		verify(bonService, never()).save(any(BonCommandeInterne.class), any(StatutBonCommandeInterne.class));
	}

	@Test
	void create_withLines_returns200() throws Exception {
		BonCommandeInterneRequestDto req = BonCommandeInterneRequestDto.builder()
				.orderDate(LocalDate.of(2026, 5, 10))
				.pointDeVenteId(11L)
				.siteId(12L)
				.userId(13L)
				.magasinCentralId(14L)
				.lines(List.of(LigneBonCommandeInterneRequestDto.builder()
						.productId(20L)
						.quantity(5)
						.unitPrice(new BigDecimal("1.50"))
						.build()))
				.build();

		BonCommandeInterne mapped = BonCommandeInterne.builder()
				.orderDate(LocalDate.of(2026, 5, 10))
				.lignes(new ArrayList<>())
				.build();
		BonCommandeInterne saved = bonEntity(101L, StatutBonCommandeInterne.BROUILLON);

		when(dtoMapper.toBon(any(BonCommandeInterneRequestDto.class))).thenReturn(mapped);
		when(bonService.save(mapped)).thenReturn(saved);
		when(dtoMapper.toBonResponse(saved)).thenReturn(sampleResponseDto(101L, StatutBonCommandeInterne.BROUILLON));

		mockMvc.perform(post("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(101));

		verify(bonService).save(mapped);
	}

	@Test
	void create_malformedJson_returns400() throws Exception {
		mockMvc.perform(post("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ not json"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Bad request"));

		verify(dtoMapper, never()).toBon(any());
	}

	@Test
	void create_whenServiceThrowsIllegalArgument_returns400() throws Exception {
		BonCommandeInterneRequestDto req = minimalCreateRequest();
		when(dtoMapper.toBon(any())).thenReturn(BonCommandeInterne.builder().lignes(new ArrayList<>()).build());
		when(bonService.save(any(BonCommandeInterne.class)))
				.thenThrow(new IllegalArgumentException("Quantité de ligne > 0 requise"));

		mockMvc.perform(post("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Quantité de ligne > 0 requise"));
	}

	@Test
	void update_withoutId_returns400() throws Exception {
		BonCommandeInterneRequestDto req = BonCommandeInterneRequestDto.builder()
				.lines(Collections.emptyList())
				.build();

		mockMvc.perform(put("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors[*].field", hasItem("id")));

		verify(dtoMapper, never()).mergeBon(any());
	}

	@Test
	void update_withoutLines_returns400() throws Exception {
		BonCommandeInterneRequestDto req = BonCommandeInterneRequestDto.builder()
				.id(5L)
				.build();

		mockMvc.perform(put("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors[*].field", hasItem("lines")));

		verify(dtoMapper, never()).mergeBon(any());
	}

	@Test
	void update_valid_returns200AndCallsMergeAndTwoArgSave() throws Exception {
		BonCommandeInterne merged = bonEntity(50L, StatutBonCommandeInterne.ENVOYE);
		BonCommandeInterneMergeResult mergeResult =
				new BonCommandeInterneMergeResult(merged, StatutBonCommandeInterne.BROUILLON);
		BonCommandeInterneRequestDto req = BonCommandeInterneRequestDto.builder()
				.id(50L)
				.lines(List.of(LigneBonCommandeInterneRequestDto.builder()
						.productId(9L)
						.quantity(3)
						.unitPrice(BigDecimal.TEN)
						.build()))
				.statut(StatutBonCommandeInterne.ENVOYE)
				.build();

		when(dtoMapper.mergeBon(any(BonCommandeInterneRequestDto.class))).thenReturn(mergeResult);
		when(bonService.save(merged, StatutBonCommandeInterne.BROUILLON)).thenReturn(merged);
		when(dtoMapper.toBonResponse(merged)).thenReturn(sampleResponseDto(50L, StatutBonCommandeInterne.ENVOYE));

		mockMvc.perform(put("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(50))
				.andExpect(jsonPath("$.statut").value("ENVOYE"));

		verify(dtoMapper).mergeBon(any(BonCommandeInterneRequestDto.class));
		verify(bonService).save(merged, StatutBonCommandeInterne.BROUILLON);
		verify(bonService, never()).save(any(BonCommandeInterne.class));
	}

	@Test
	void update_invalidStatutEnumInJson_returns400() throws Exception {
		String body = """
				{"id":1,"lines":[],"statut":"NOT_A_REAL_STATUS"}
				""";

		mockMvc.perform(put("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isBadRequest());

		verify(dtoMapper, never()).mergeBon(any());
	}

	@Test
	void update_whenServiceRejectsTransition_returns400() throws Exception {
		BonCommandeInterne merged = bonEntity(60L, StatutBonCommandeInterne.TRAITE);
		BonCommandeInterneMergeResult mergeResult =
				new BonCommandeInterneMergeResult(merged, StatutBonCommandeInterne.BROUILLON);

		when(dtoMapper.mergeBon(any())).thenReturn(mergeResult);
		when(bonService.save(merged, StatutBonCommandeInterne.BROUILLON))
				.thenThrow(new IllegalArgumentException("Depuis BROUILLON : seuls ENVOYE et ANNULE sont autorisés"));

		BonCommandeInterneRequestDto req = BonCommandeInterneRequestDto.builder()
				.id(60L)
				.lines(Collections.emptyList())
				.statut(StatutBonCommandeInterne.TRAITE)
				.build();

		mockMvc.perform(put("/api/bons-commande-interne")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("autorisés")));
	}

	@Test
	void delete_callsLoadThenRemove() throws Exception {
		BonCommandeInterne b = bonEntity(3L, StatutBonCommandeInterne.BROUILLON);
		when(bonService.loadById(3L)).thenReturn(b);

		mockMvc.perform(delete("/api/bons-commande-interne/3")).andExpect(status().isOk());

		verify(bonService).loadById(3L);
		verify(bonService).remove(b);
	}

	@Test
	void delete_whenRemoveThrows_returns400() throws Exception {
		BonCommandeInterne b = bonEntity(4L, StatutBonCommandeInterne.ENVOYE);
		when(bonService.loadById(4L)).thenReturn(b);
		doThrow(new IllegalArgumentException("Suppression réservée aux bons en BROUILLON"))
				.when(bonService)
				.remove(b);

		mockMvc.perform(delete("/api/bons-commande-interne/4"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Suppression réservée aux bons en BROUILLON"));

		verify(bonService).remove(b);
	}

	@Test
	void delete_invalidPath_returns400() throws Exception {
		mockMvc.perform(delete("/api/bons-commande-interne/bad")).andExpect(status().isBadRequest());
		verify(bonService, never()).remove(any());
	}

	private static BonCommandeInterneRequestDto minimalCreateRequest() {
		return BonCommandeInterneRequestDto.builder()
				.orderDate(LocalDate.of(2026, 5, 10))
				.pointDeVenteId(11L)
				.siteId(12L)
				.userId(13L)
				.magasinCentralId(14L)
				.build();
	}

	private static BonCommandeInterne bonEntity(long id, StatutBonCommandeInterne statut) {
		BonCommandeInterne b = BonCommandeInterne.builder()
				.number("N-" + id)
				.orderDate(LocalDate.of(2026, 5, 1))
				.statut(statut)
				.lignes(new ArrayList<>())
				.build();
		b.setId(id);
		return b;
	}

	private static BonCommandeInterneResponseDto sampleResponseDto(long id, StatutBonCommandeInterne statut) {
		return BonCommandeInterneResponseDto.builder()
				.id(id)
				.number("N-" + id)
				.orderDate(LocalDate.of(2026, 5, 1))
				.statut(statut)
				.statutLibelle(statut.getLibelle())
				.commentaire(null)
				.site(EntityRefDto.builder().id(1L).code("S").label("Site").build())
				.pointDeVente(EntityRefDto.builder().id(2L).code("P").label("PDV").build())
				.user(EntityRefDto.builder().id(3L).code("u").label("User").build())
				.magasinCentral(EntityRefDto.builder().id(4L).code("M").label("Mag").build())
				.lines(List.of(LigneBonCommandeInterneResponseDto.builder()
						.id(99L)
						.quantity(1)
						.unitPrice(BigDecimal.ONE)
						.product(EntityRefDto.builder().id(8L).code("CB").label("Prod").build())
						.build()))
				.build();
	}
}
