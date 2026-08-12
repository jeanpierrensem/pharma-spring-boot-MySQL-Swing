package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface PackagingService extends IService<Packaging> {
    Packaging loadByCode(String code);

    List<Packaging> findByCriteria(String code, String description);
}
