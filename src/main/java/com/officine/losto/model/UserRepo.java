package com.officine.losto.model;

import com.officine.losto.entity.*;
import com.officine.losto.security.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface UserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findFirstByLoginOrderByIdAsc(String login);

    default Optional<AppUser> findOptionalByLogin(String login) {
        return findFirstByLoginOrderByIdAsc(LoginNormalizer.normalize(login));
    }

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.group g LEFT JOIN FETCH g.menus WHERE u.login = :login")
    Optional<AppUser> findWithGroupMenusByLogin(@Param("login") String login);

    default Optional<AppUser> findWithGroupMenusByLoginNormalized(String login) {
        return findWithGroupMenusByLogin(LoginNormalizer.normalize(login));
    }

    default AppUser findByLogin(String login) {
        return findOptionalByLogin(login).orElse(null);
    }

    boolean existsByLogin(String login);

    default boolean existsByLoginNormalized(String login) {
        String normalized = LoginNormalizer.normalize(login);
        return normalized != null && existsByLogin(normalized);
    }

    boolean existsByEmail(String email);

    List<AppUser> findByGroup_Id(Long groupId);

    AppUser findByName(String usernam);

    List<AppUser> findByNameContainingOrLoginContainingOrEmailContaining(String name, String login, String email);
}
