package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface ReceiptDetailsRepo extends JpaRepository<ReceiptDetails, Long> {
    long deleteByOrdersDetails(OrdersDetails ordersDetails);

    List<ReceiptDetails> findByOrdersDetails(OrdersDetails ordersDetails);
    //List<ReceiptDetails> findByOrders(Orders orders);
    /* @Query("SELECT r FROM ReceiptDetails r WHERE r.commandeLigne.id = :id")
     List<ReceiptDetails> findByCommandeLigneId(@Param("id") Long id);*/
}
