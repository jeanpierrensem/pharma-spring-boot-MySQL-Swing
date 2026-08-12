package com.officine.losto.s5.reappro.dto;

import com.officine.losto.entity.BonCommandeInterne;
import com.officine.losto.entity.StatutBonCommandeInterne;

/**
 * Résultat de fusion DTO → entité : le statut avant modification sert à valider les transitions
 * dans {@link com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl}.
 */
public record BonCommandeInterneMergeResult(
		BonCommandeInterne bon, StatutBonCommandeInterne statutAvantFusion) {}
