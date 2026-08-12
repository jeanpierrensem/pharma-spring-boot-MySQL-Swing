package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCode(String code);

    List<Category> findByDescriptionContainingOrCodeContaining(String description, String code);
}
