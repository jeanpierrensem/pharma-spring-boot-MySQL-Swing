package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Product findByCodeBar(String codeBar);

    List<Product> findByNameContainingOrCodeBarContaining(String name, String codeBar);

    List<Product> findBySection_Id(Long sectionId);


    @Query("select distinct p from Product p join p.thresholds th where th.id = :thresholdId")
    List<Product> findDistinctByThresholdId(@Param("thresholdId") Long thresholdId);
}
