package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@Transactional
@Repository
public interface OrderDetailsService extends IService<OrdersDetails> {
    OrdersDetails loadByOrders(Orders orders);

    List<OrdersDetails> findByCriteria(String code, String description);
}
