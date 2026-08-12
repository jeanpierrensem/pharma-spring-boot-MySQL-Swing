package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface ThresholdService extends IService<Threshold> {
    Threshold loadByName(String name);

    List<Threshold> findByCriteria(String name, String description);
}
