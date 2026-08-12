package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface MenuRepo extends JpaRepository<Menu, Long> {
    Menu findByName(String name);

    Optional<Menu> findByPathCode(String pathCode);

    List<Menu> findByDescriptionContainingOrNameContaining(String description, String name);

    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.groups")
    List<Menu> findAllWithGroups();

    /**
     * Racines avec premier niveau d’enfants (évite {@code MultipleBagFetchException} si on enchaîne
     * plusieurs {@code JOIN FETCH} sur la même collection {@code List} {@link Menu#getChildren()}).
     */
    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.children WHERE m.parent IS NULL")
    List<Menu> findRootsWithImmediateChildren();

    /**
     * Charge le sous-arbre immédiat pour les menus de second niveau (onglets → actions).
     */
    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.children WHERE m.id IN :ids")
    List<Menu> findByIdInWithChildrenFetched(@Param("ids") Collection<Long> ids);
}
