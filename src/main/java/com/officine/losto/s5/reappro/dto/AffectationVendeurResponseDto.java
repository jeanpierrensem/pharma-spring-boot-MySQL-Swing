package com.officine.losto.s5.reappro.dto;

import com.officine.losto.dto.EntityRefDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffectationVendeurResponseDto {
	private Long id;
	private LocalDateTime debut;
	private LocalDateTime fin;
	private Boolean actifCreneau;
	private EntityRefDto appUser;
	private EntityRefDto pointDeVente;
}
