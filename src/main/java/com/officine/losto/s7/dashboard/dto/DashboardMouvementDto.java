package com.officine.losto.s7.dashboard.dto;

import java.time.LocalDateTime;

import com.officine.losto.s7.stocks.domain.TypeMouvementStock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMouvementDto {
	private Long id;
	private TypeMouvementStock typeMouvement;
	private String productName;
	private Integer quantiteAlgebrique;
	private LocalDateTime dateMouvement;
	private String commentaire;
}
