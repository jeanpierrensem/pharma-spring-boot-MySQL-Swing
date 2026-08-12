package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface SellDetailsService extends IService<SellDetails> {
    List<SellDetails> loadBySell(Sell sell);
    //List<SellDetails> findByCriteria( Sell sell,  Product product);
}
