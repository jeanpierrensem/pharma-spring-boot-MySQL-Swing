package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface OrderDetailsRepo extends JpaRepository<OrdersDetails, Long> {

    Optional<OrdersDetails> findById(Long id);

    long deleteByOrders(Orders order);

    OrdersDetails findByOrders(Orders order);
       
    /* @Query("SELECT r FROM Receipt_details r WHERE r.commandeLigne.id = :id")
     List<Receipt_details> findByCommandeLigneId(@Param("id") Long id);
     */

}
