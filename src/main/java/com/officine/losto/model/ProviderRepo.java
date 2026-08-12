package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface ProviderRepo extends JpaRepository<Provider, Long> {
    Provider findByCode(String code);

    List<Provider> findByPhoneNumberContainingOrCodeContaining(String phoneNumber, String code);
}

