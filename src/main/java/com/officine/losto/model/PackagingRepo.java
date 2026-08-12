package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface PackagingRepo extends JpaRepository<Packaging, Long> {
    Packaging findByCode(String code);

    List<Packaging> findByDescriptionContainingOrCodeContaining(String description, String code);
}
