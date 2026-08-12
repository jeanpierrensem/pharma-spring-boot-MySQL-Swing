package com.officine.losto.s7.pilotage.dto;

import java.time.LocalDate;

/**
 * Période optionnellement restreinte au site (diagramme s7). Les deux bornes sont inclusives.
 */
public record PeriodeFiltre(LocalDate dtDebut, LocalDate dtFin, Long siteIdOptionnel) {}
