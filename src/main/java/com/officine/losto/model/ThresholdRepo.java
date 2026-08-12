package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface ThresholdRepo extends JpaRepository<Threshold, Long> {
    Threshold findByCode(String code);

    List<Threshold> findByCodeContainingOrDescriptionContaining(String code, String description);
}
