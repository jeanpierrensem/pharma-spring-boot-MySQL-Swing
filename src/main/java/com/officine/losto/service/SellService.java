package com.officine.losto.service;

import com.officine.losto.entity.*;

import java.time.*;
import java.util.*;

public interface SellService extends IService<Sell> {
    Sell loadByCode(String number);

    List<Sell> findByCriteria(String number, String seller, String client, String sellType);

    /**
     * Filtre par site, point de caisse, période (date de vente), et utilisateur (vendeur) ayant enregistré.
     */
    List<Sell> findFiltered(
            Long siteId, Long pointDeVenteId, LocalDate from, LocalDate to, Long effectueeParUserId);
}
