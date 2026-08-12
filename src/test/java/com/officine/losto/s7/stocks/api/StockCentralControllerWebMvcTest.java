package com.officine.losto.s7.stocks.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.officine.losto.exception.RestExceptionHandler;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s7.stocks.domain.TypeMouvementStock;
import com.officine.losto.s7.stocks.dto.StockCentralAdjustDisponibleDto;
import com.officine.losto.s7.stocks.dto.StockCentralResponseDto;
import com.officine.losto.s7.stocks.service.StockCentralService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockCentralController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
@ActiveProfiles("test")
class StockCentralControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private StockCentralService stockCentralService;

	@Test
	void getAll_returnsJsonList() throws Exception {
		when(stockCentralService.listAll()).thenReturn(List.of(StockCentralResponseDto.builder()
				.id(1L)
				.siteId(10L)
				.magasinCentralId(20L)
				.productId(30L)
				.qteDisponible(100)
				.qteReservee(5)
				.qteSeuilAlerte(10)
				.updatedAt(LocalDateTime.parse("2026-04-01T12:00:00"))
				.build()));

		mockMvc.perform(get("/api/stock-central"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].magasinCentralId").value(20))
				.andExpect(jsonPath("$[0].productId").value(30))
				.andExpect(jsonPath("$[0].qteDisponible").value(100));
	}

	@Test
	void getById_notFound_returns404() throws Exception {
		when(stockCentralService.getById(404L)).thenThrow(new ResourceNotFoundException("StockCentral", 404L));

		mockMvc.perform(get("/api/stock-central/404"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Not found"));
	}

	@Test
	void postAdjustDisponible_validBody_callsService() throws Exception {
		StockCentralAdjustDisponibleDto body = StockCentralAdjustDisponibleDto.builder()
				.magasinCentralId(2L)
				.productId(3L)
				.batchId(4L)
				.delta(5)
				.typeMouvement(TypeMouvementStock.ENTREE)
				.build();
		when(stockCentralService.adjustDisponible(any(StockCentralAdjustDisponibleDto.class)))
				.thenReturn(StockCentralResponseDto.builder()
						.id(99L)
						.siteId(1L)
						.magasinCentralId(2L)
						.productId(3L)
						.qteDisponible(15)
						.qteReservee(0)
						.build());

		mockMvc.perform(post("/api/stock-central/adjust-disponible")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(body)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(99))
				.andExpect(jsonPath("$.qteDisponible").value(15));

		verify(stockCentralService).adjustDisponible(any(StockCentralAdjustDisponibleDto.class));
	}

	@Test
	void postAdjustDisponible_missingRequired_returns400() throws Exception {
		mockMvc.perform(post("/api/stock-central/adjust-disponible")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}
