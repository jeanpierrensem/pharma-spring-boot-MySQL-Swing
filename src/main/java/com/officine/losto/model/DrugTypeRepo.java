package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface DrugTypeRepo extends JpaRepository<DrugType, Long> {
    DrugType findByCode(String code);

    List<DrugType> findByDescriptionContainingOrCodeContaining(String description, String code);
}
