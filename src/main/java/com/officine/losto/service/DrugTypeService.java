package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface DrugTypeService extends IService<DrugType> {
    DrugType loadByCode(String code);

    List<DrugType> findByCriteria(String code, String description);
}
