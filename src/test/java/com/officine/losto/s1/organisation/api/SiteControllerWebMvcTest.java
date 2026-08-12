package com.officine.losto.s1.organisation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.exception.RestExceptionHandler;
import com.officine.losto.s1.organisation.dto.SiteRequestDto;
import com.officine.losto.s1.organisation.dto.SiteResponseDto;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
import com.officine.losto.s1.organisation.service.SiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SiteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
@ActiveProfiles("test")
class SiteControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private SiteService siteService;

	@MockitoBean
	private OrganisationMapper organisationMapper;

	@Test
	void getAll_returnsJsonList() throws Exception {
		Site site = Site.builder()
				.id(10L)
				.code("S-WEB")
				.libelle("Site WebMvc")
				.actif(true)
				.build();
		when(siteService.findAll()).thenReturn(List.of(site));
		when(siteService.buildResponse(site)).thenReturn(SiteResponseDto.builder()
				.id(10L)
				.code("S-WEB")
				.libelle("Site WebMvc")
				.actif(true)
				.magasinCentralId(null)
				.build());

		mockMvc.perform(get("/api/sites"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].code").value("S-WEB"))
				.andExpect(jsonPath("$[0].libelle").value("Site WebMvc"))
				.andExpect(jsonPath("$[0].actif").value(true));
	}

	@Test
	void post_withoutRequiredFields_returns400() throws Exception {
		mockMvc.perform(post("/api/sites")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void post_validBody_callsService() throws Exception {
		SiteRequestDto req = SiteRequestDto.builder()
				.code("S-NEW")
				.libelle("Nouveau site")
				.actif(true)
				.build();
		Site saved = Site.builder()
				.id(99L)
				.code("S-NEW")
				.libelle("Nouveau site")
				.actif(true)
				.build();
		when(siteService.create(any(SiteRequestDto.class))).thenReturn(saved);
		when(siteService.buildResponse(saved)).thenReturn(SiteResponseDto.builder()
				.id(99L)
				.code("S-NEW")
				.libelle("Nouveau site")
				.actif(true)
				.build());

		mockMvc.perform(post("/api/sites")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(99))
				.andExpect(jsonPath("$.code").value("S-NEW"));

		verify(siteService).create(any(SiteRequestDto.class));
	}

	@Test
	void getPointsDeVenteBySite_mapsThroughMapper() throws Exception {
		Site site = Site.builder().id(1L).code("S").libelle("L").actif(true).build();
		PointDeVente pdv = PointDeVente.builder()
				.id(5L)
				.site(site)
				.code("PDV")
				.libelle("Caisse")
				.actif(true)
				.build();
		when(siteService.findPointsDeVenteBySite(1L)).thenReturn(List.of(pdv));
		when(organisationMapper.toPdvResponse(pdv)).thenReturn(
				com.officine.losto.s1.organisation.dto.PointDeVenteResponseDto.builder()
						.id(5L)
						.siteId(1L)
						.code("PDV")
						.libelle("Caisse")
						.actif(true)
						.build());

		mockMvc.perform(get("/api/sites/1/points-de-vente"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].code").value("PDV"));
	}
}
