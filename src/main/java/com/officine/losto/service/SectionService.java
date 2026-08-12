package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@Transactional
public interface SectionService extends IService<Section> {
    Section loadByCode(String code);

    List<Section> findByCriteria(String code, String description);
}
