package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@Transactional
public interface FormService extends IService<Form> {
    Form loadByCode(String code);

    List<Form> findByCriteria(String code, String description);
}
