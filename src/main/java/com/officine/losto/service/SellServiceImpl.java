package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import com.officine.losto.s4.ventes.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

@Service
public class SellServiceImpl implements SellService {
    private final SellRepo sellRepo;

    public SellServiceImpl(SellRepo sellRepo) {
        this.sellRepo = sellRepo;
    }

    /**
     * Règles minimales. Phase 2 (stock) : alimenter {@code mouvement_stock} / sortie
     * {@code stock_pdv} à partir du ticket (réf. type SELL, id = vente) — non implémenté ici.
     */
    private static void validateSell(Sell s) {
        if (s.getLignes() == null || s.getLignes().isEmpty()) {
            throw new IllegalArgumentException("La vente doit comporter au moins une ligne produit");
        }
        for (SellDetails d : s.getLignes()) {
            if (d.getQuantity() == null || d.getQuantity() <= 0) {
                throw new IllegalArgumentException("Chaque ligne doit avoir une quantité strictement positive");
            }
            if (d.getPrice() == null || d.getPrice().signum() < 0) {
                throw new IllegalArgumentException("Prix de ligne négatif interdit");
            }
        }
        if (s.getTotalPrice() != null && s.getTotalPrice().signum() < 0) {
            throw new IllegalArgumentException("Montant total de vente négatif interdit");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sell> getAll() {
        return sellRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Sell loadById(long id) {
        return sellRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Sell save(Sell sell) {
        validateSell(sell);
        return sellRepo.save(sell);
    }

    @Override
    @Transactional
    public Sell update(Sell sell) {
        validateSell(sell);
        return sellRepo.save(sell);
    }

    @Override
    @Transactional
    public List<Sell> saveAll(List<Sell> sells) {
        for (Sell s : sells) {
            validateSell(s);
        }
        return sellRepo.saveAll(sells);
    }

    @Override
    @Transactional
    public Sell saveAndFlush(Sell sell) {
        validateSell(sell);
        return sellRepo.saveAndFlush(sell);
    }

    @Override
    @Transactional
    public List<Sell> saveAllAndFlush(List<Sell> sells) {
        for (Sell s : sells) {
            validateSell(s);
        }
        return sellRepo.saveAllAndFlush(sells);
    }

    @Override
    @Transactional
    public void remove(Sell sell) {
        sellRepo.delete(sell);
    }

    @Override
    @Transactional(readOnly = true)
    public Sell loadByCode(String number) {
        return sellRepo.findByNumber(number);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sell> findByCriteria(String number, String seller, String client, String sellType) {
        return sellRepo.findAll(SellSpecification.searchText(number, seller, client, sellType));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sell> findFiltered(
            Long siteId, Long pointDeVenteId, LocalDate from, LocalDate to, Long effectueeParUserId) {
        return sellRepo.findAll(SellSpecification.filter(siteId, pointDeVenteId, from, to, effectueeParUserId));
    }
}
