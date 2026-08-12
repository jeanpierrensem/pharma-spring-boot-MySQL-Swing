package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

@Transactional
public interface ReceiptDetailsService extends IService<ReceiptDetails> {
    //ReceiptDetails loadByCode (String code);
    /*List<ReceiptDetails> findByOrders( Orders orders);*/
}
