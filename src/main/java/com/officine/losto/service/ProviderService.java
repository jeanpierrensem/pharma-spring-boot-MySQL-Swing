package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface ProviderService extends IService<Provider> {
    Provider loadByCode(String code);

    List<Provider> findByCriteria(String code, String designation);
}
