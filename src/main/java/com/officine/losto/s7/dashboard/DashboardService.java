package com.officine.losto.s7.dashboard;

import java.time.LocalDate;

import com.officine.losto.s7.dashboard.dto.DashboardSyntheseDto;

public interface DashboardService {

	DashboardSyntheseDto synthese(LocalDate dtDebut, LocalDate dtFin, Long siteIdOptionnel);
}
