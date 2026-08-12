package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@Transactional
public interface BatchService extends IService<Batch> {
    Batch loadByCode(String code);

    List<Batch> findByCriteria(String code);

    List<Batch> findByProviderId(long providerId);
}
