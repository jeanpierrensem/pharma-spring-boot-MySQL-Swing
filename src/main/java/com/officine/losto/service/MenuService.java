package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@Transactional
public interface MenuService extends IService<Menu> {
    Menu loadByName(String name);

    List<Menu> findByCriteria(String name, String description);

    /**
     * Arbre habilitation (profondeur 3) : racines avec enfants chargés.
     */
    List<Menu> loadHabilitationMenuTree();

    /**
     * Met à jour la table MENU à partir du catalogue applicatif ({@code MenuSecurityCatalog}).
     */
    int syncHabilitationCatalog();
}
