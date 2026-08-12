package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;


public interface FormRepo extends JpaRepository<Form, Long> {
    Form findByCode(String code);

    List<Form> findByDescriptionContainingOrCodeContaining(String description, String code);
}
