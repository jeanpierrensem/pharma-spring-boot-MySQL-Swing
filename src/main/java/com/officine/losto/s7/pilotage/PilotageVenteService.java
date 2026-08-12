package com.officine.losto.s7.pilotage;

import java.util.List;

import com.officine.losto.s7.pilotage.dto.ClassementProduit;
import com.officine.losto.s7.pilotage.dto.PeriodeFiltre;
import com.officine.losto.s7.pilotage.dto.ResultatAnneePic;
import com.officine.losto.s7.pilotage.dto.ResultatCaParPdv;
import com.officine.losto.s7.pilotage.dto.ResultatMargeProduitLot;
import com.officine.losto.s7.pilotage.dto.ResultatMoisPic;

/** Agrégats ventes pour le sous-système s7_pilotage. */
public interface PilotageVenteService {

	List<ResultatCaParPdv> caParPointDeVente(PeriodeFiltre filtre);

	List<ResultatMargeProduitLot> margesParProduitEtLot(PeriodeFiltre filtre);

	List<ClassementProduit> topProduitsParCa(PeriodeFiltre filtre, int limit, PilotageProduitSort sort);

	List<ResultatMoisPic> moisLesPlusActifs(int annee, Long siteIdOptionnel);

	List<ResultatAnneePic> anneesLesPlusActives(PeriodeFiltre filtre);
}
