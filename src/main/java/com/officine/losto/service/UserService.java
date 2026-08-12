package com.officine.losto.service;

import com.officine.losto.entity.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
public interface UserService extends IService<AppUser> {
    AppUser loadByName(String name);
    List<AppUser> findByCriteria(String name, String login, String email);
    /**
     * Users matching {@code ids} in the same order; entries with no matching row are skipped.
     */
    List<AppUser> findAllByIdsInOrder(List<Long> ids);
}
