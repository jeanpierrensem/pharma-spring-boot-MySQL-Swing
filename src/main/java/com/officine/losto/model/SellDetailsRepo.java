package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface SellDetailsRepo extends JpaRepository<SellDetails, Long> {
    //long deleteByLigneVente(LigneVente ligneVente );
    List<SellDetails> findBySell(Sell sell);
    /// List<SellDetails> findBySellContainingOrProductContaining(Sell sell, Product product);
     /*@Query("SELECT r FROM Sell_details r WHERE r.sell.id = :id")
     List<Sell_details> findById(@Param("id") Long id);*/
}

