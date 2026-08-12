package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface BatchRepo extends JpaRepository<Batch, Long> {
    Batch findByNumber(String number);

    List<Batch> findByNumberContaining(String number);

    List<Batch> findByProvider_Id(Long providerId);
}
