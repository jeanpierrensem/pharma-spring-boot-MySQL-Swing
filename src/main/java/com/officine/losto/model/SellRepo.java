package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.*;

import java.util.*;

public interface SellRepo extends JpaRepository<Sell, Long>, JpaSpecificationExecutor<Sell> {

    @EntityGraph(type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD, attributePaths = {
            "lignes", "lignes.product", "site", "pointDeVente", "effectueePar"
    })
    @NonNull
    @Override
    Optional<Sell> findById(@NonNull Long id);

    @EntityGraph(type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD, attributePaths = {
            "lignes", "lignes.product", "site", "pointDeVente", "effectueePar"
    })
    Sell findByNumber(String number);

    boolean existsByNumber(String number);

    @EntityGraph(type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD, attributePaths = {
            "lignes", "lignes.product", "site", "pointDeVente", "effectueePar"
    })
    @NonNull
    @Override
    List<Sell> findAll();

    @EntityGraph(type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD, attributePaths = {
            "lignes", "lignes.product", "site", "pointDeVente", "effectueePar"
    })
    @NonNull
    @Override
    List<Sell> findAll(Specification<Sell> spec);
}
