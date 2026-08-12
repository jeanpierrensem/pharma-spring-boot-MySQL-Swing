package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface GroupService extends IService<AppGroup> {
    AppGroup loadByName(String name);

    List<AppGroup> findByCriteria(String name, String description);

    void assignMenusToGroup(Long groupId, List<Long> menuIds);
}
