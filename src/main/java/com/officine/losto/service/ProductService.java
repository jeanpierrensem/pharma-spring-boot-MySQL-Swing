package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface ProductService extends IService<Product> {
    List<Product> findAllByIdsInOrder(List<Long> ids);

    List<Product> findByThresholdId(long thresholdId);

    List<Product> findBySectionId(long sectionId);

    List<Product> findByBatchId(long batchId);

    Product loadByCode(String code);

    List<Product> findByCriteria(String code, String name);

    void incrementProductWarehouseQuantity(Sell sell);

    void decrementProductWarehouseQuantity(Sell sell);
}
