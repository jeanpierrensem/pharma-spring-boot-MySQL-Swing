package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface CategoryService extends IService<Category> {
    Category loadByCode(String code);

    List<Category> findByCriteria(String code, String description);
}
