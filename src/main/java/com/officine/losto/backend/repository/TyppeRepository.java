package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Typpe;


public interface TyppeRepository extends JpaRepository<Typpe, Long> {
     Typpe findByTyppeName (String typeName) ;
     Optional<Typpe> findById(Long id); 
     List<Typpe> findByTyppeDescriptionContainingOrTyppeNameContaining(String typpeDescription, String typpeName); 
     
     

}
