package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface SectionRepo extends JpaRepository<Section, Long> {
    Section findByCode(String code);

    List<Section> findByDescriptionContainingOrCodeContaining(String description, String code);
}
