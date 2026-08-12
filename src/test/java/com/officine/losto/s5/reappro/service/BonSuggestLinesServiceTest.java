package com.officine.losto.s5.reappro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Product;
import com.officine.losto.entity.StockPdv;
import com.officine.losto.entity.Threshold;
import com.officine.losto.model.ProductRepo;
import com.officine.losto.model.ThresholdRepo;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s5.reappro.dto.SuggestBonLineResponseDto;
import com.officine.losto.s7.stocks.repository.StockPdvRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BonSuggestLinesServiceTest {

	@Mock
	private StockPdvRepository stockPdvRepository;

	@Mock
	private PointDeVenteRepository pointDeVenteRepository;

	@Mock
	private ThresholdRepo thresholdRepo;

	@Mock
	private ProductRepo productRepo;

	@InjectMocks
	private BonSuggestLinesService service;

	@Test
	void suggestLines_includesOnlyProductsBelowAlertThreshold() {
		when(pointDeVenteRepository.existsById(11L)).thenReturn(true);
		when(thresholdRepo.findByCode("THR-STOCK-BAS"))
				.thenReturn(Threshold.builder().code("THR-STOCK-BAS").level(10).build());

		Product low = Product.builder().name("Produit bas").build();
		low.setId(100L);
		Product ok = Product.builder().name("Produit ok").build();
		ok.setId(101L);

		StockPdv rowLow =
				StockPdv.builder()
						.pointDeVente(PointDeVente.builder().id(11L).build())
						.product(low)
						.qteDisponible(3)
						.qteSeuilAlerte(8)
						.build();
		StockPdv rowOk =
				StockPdv.builder()
						.pointDeVente(PointDeVente.builder().id(11L).build())
						.product(ok)
						.qteDisponible(20)
						.qteSeuilAlerte(8)
						.build();

		when(stockPdvRepository.findByPointDeVente_Id(11L)).thenReturn(List.of(rowLow, rowOk));
		when(productRepo.findById(100L)).thenReturn(Optional.of(low));

		List<SuggestBonLineResponseDto> lines = service.suggestLinesForPointDeVente(11L);

		assertThat(lines).hasSize(1);
		assertThat(lines.get(0).getProductId()).isEqualTo(100L);
		assertThat(lines.get(0).getQuantity()).isEqualTo(5);
	}

	@Test
	void defaultOrderQuantity_isZeroWhenStockAboveThreshold() {
		when(pointDeVenteRepository.existsById(11L)).thenReturn(true);
		when(thresholdRepo.findByCode("THR-STOCK-BAS"))
				.thenReturn(Threshold.builder().code("THR-STOCK-BAS").level(10).build());

		Product p = Product.builder().name("Surstock").build();
		p.setId(102L);
		StockPdv row =
				StockPdv.builder()
						.pointDeVente(PointDeVente.builder().id(11L).build())
						.product(p)
						.qteDisponible(15)
						.qteSeuilAlerte(8)
						.build();
		when(stockPdvRepository.findByPointDeVente_Id(11L)).thenReturn(List.of(row));

		assertThat(service.suggestLinesForPointDeVente(11L)).isEmpty();
	}
}
