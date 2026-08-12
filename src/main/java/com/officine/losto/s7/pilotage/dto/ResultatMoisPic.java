package com.officine.losto.s7.pilotage.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultatMoisPic {
	private int annee;
	private int mois;
	private BigDecimal chiffreAffaires;
	private int rang;
}
