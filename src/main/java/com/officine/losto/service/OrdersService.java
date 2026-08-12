package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface OrdersService extends IService<Orders> {
    Orders loadByCode(String code);

    List<Orders> findByCriteria(String number, String orderDate, String description);
}
